package pl.codecity.main.controller.admin.user;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codecity.main.model.UserInvitation;
import pl.codecity.main.service.UserService;
import pl.codecity.main.utility.AuthorizedUser;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.validation.Valid;

@Controller
@RequestMapping("/{language}/users/invitations/resend")
public class UserInvitationResendController {

	@Inject
	private UserService userService;

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@RequestMapping
	public String save(
			@PathVariable String language,
			@Valid @ModelAttribute("form") UserInvitationResendForm form,
			BindingResult result,
			String query,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes) throws MessagingException {
		UserInvitation invitation = userService.inviteAgain(form.buildUserInvitationResendRequest(), result, authorizedUser);
		redirectAttributes.addFlashAttribute("resentInvitation", invitation);
		redirectAttributes.addAttribute("query", query);
		return "redirect:/_admin/{language}/users/invitations/index";
	}
}
