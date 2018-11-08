package pl.codecity.main.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codecity.main.exception.DuplicateEmailException;
import pl.codecity.main.exception.DuplicateLoginIdException;
import pl.codecity.main.model.User;
import pl.codecity.main.request.ProfileUpdateRequest;
import pl.codecity.main.service.UserService;
import pl.codecity.main.utility.AuthorizedUser;

import javax.inject.Inject;

@Controller
@RequestMapping("/settings/profile")
public class ProfileUpdateController {

	public static final String FORM_MODEL_KEY = "form";
	public static final String ERRORS_MODEL_KEY = BindingResult.MODEL_KEY_PREFIX + FORM_MODEL_KEY;

	@Inject
	private UserService userService;

	@ModelAttribute(FORM_MODEL_KEY)
	public ProfileUpdateForm setupProfileUpdateForm() {
		return new ProfileUpdateForm();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String init(
			AuthorizedUser authorizedUser,
			Model model) {
		User user = userService.getUserById(authorizedUser.getId());

		ProfileUpdateForm form = new ProfileUpdateForm();
		form.setEmail(user.getEmail());
		form.setLoginId(user.getLoginId());
		ProfileUpdateForm.Name name = new ProfileUpdateForm.Name();
		name.setFirstName(user.getName().getFirstName());
		name.setLastName(user.getName().getLastName());
		form.setName(name);
		model.addAttribute(FORM_MODEL_KEY, form);
		return edit(model);
	}

	@RequestMapping(method = RequestMethod.GET, params = "step.edit")
	public String edit(Model model) {
		return "user/profile-update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(
			@Validated @ModelAttribute(FORM_MODEL_KEY) ProfileUpdateForm form,
			BindingResult errors,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(FORM_MODEL_KEY, form);
		redirectAttributes.addFlashAttribute(ERRORS_MODEL_KEY, errors);

		if (errors.hasErrors()) {
			return "redirect:/settings/profile?step.edit";
		}

		ProfileUpdateRequest request = new ProfileUpdateRequest()
				.withUserId(authorizedUser.getId())
				.withEmail(form.getEmail())
				.withLoginId(form.getLoginId())
				.withName(form.getName().getFirstName(), form.getName().getLastName());

		User updatedUser;
		try {
			updatedUser = userService.updateProfile(request, authorizedUser);
		} catch (DuplicateLoginIdException e) {
			errors.rejectValue("loginId", "NotDuplicate");
			return "redirect:/settings/profile?step.edit";
		} catch (DuplicateEmailException e) {
			errors.rejectValue("email", "NotDuplicate");
			return "redirect:/settings/profile?step.edit";
		}

		redirectAttributes.getFlashAttributes().clear();
		redirectAttributes.addFlashAttribute("updatedUser", updatedUser);
		return "redirect:/settings/profile";
	}
}
