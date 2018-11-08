package pl.codecity.main.controller.admin.signup;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.codecity.main.controller.support.HttpForbiddenException;
import pl.codecity.main.exception.DuplicateEmailException;
import pl.codecity.main.exception.DuplicateLoginIdException;
import pl.codecity.main.model.User;
import pl.codecity.main.model.UserInvitation;
import pl.codecity.main.service.SignupService;

import javax.inject.Inject;
import javax.validation.Valid;

@Controller
@RequestMapping("/signup")
public class SignupController {

	@Inject
	private SignupService signupService;

	@ModelAttribute("form")
	public SignupForm signupForm(@RequestParam String token) {
		UserInvitation invitation = signupService.readUserInvitation(token);
		boolean valid = signupService.validateInvitation(invitation);
		if (!valid) {
			throw new HttpForbiddenException();
		}
		SignupForm form = new SignupForm();
		form.setToken(token);
		form.setEmail(invitation.getEmail());
		return form;
	}

	@RequestMapping(method=RequestMethod.GET)
	public String signup() {
		return "signup/signup";
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(
			@Valid @ModelAttribute("form") SignupForm form,
			BindingResult errors) {
		if (errors.hasErrors()) {
			return "signup/signup";
		}

		try {
			signupService.signup(form.toSignupRequest(), User.Role.ADMIN, form.getToken());
		} catch (DuplicateLoginIdException e) {
			errors.rejectValue("loginId", "NotDuplicate");
			return "signup/signup";
		} catch (DuplicateEmailException e) {
			errors.rejectValue("email", "NotDuplicate");
			return "signup/signup";
		}

		return "redirect:/_admin/login";
	}
}
