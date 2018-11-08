package pl.codecity.main.controller.admin.user;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.domain.UserInvitation;
import org.wallride.service.UserService;
import org.wallride.support.AuthorizedUser;
import pl.codecity.main.model.UserInvitation;
import pl.codecity.main.service.UserService;
import pl.codecity.main.utility.AuthorizedUser;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/{language}/users/invitations/create")
public class UserInvitationCreateController {

	@Inject
	private UserService userService;

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@RequestMapping(method= RequestMethod.POST)
	public String save(
			@PathVariable String language,
			@Valid @ModelAttribute("form") UserInvitationCreateForm form,
			BindingResult result,
			String query,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes) throws MessagingException {
		if (result.hasErrors()) {
			return "user/invitation/index";
		}
		List<UserInvitation> invitations = userService.inviteUsers(form.buildUserInvitationCreateRequest(), result, authorizedUser);
		redirectAttributes.addFlashAttribute("savedInvitations", invitations);
		redirectAttributes.addAttribute("query", query);
		return "redirect:/_admin/{language}/users/invitations/index";
	}
}
