package pl.codecity.main.controller.admin.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.codecity.main.model.UserInvitation;
import pl.codecity.main.service.UserService;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping("/{language}/users/invitations/index")
public class UserInvitationIndexController {

	@Inject
	private UserService userService;

	@ModelAttribute("invitations")
	public List<UserInvitation> userInvitations() {
		return userService.getUserInvitations();
	}

	@ModelAttribute("form")
	public UserInvitationCreateForm userInviteForm() {
		return new UserInvitationCreateForm();
	}

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@RequestMapping(method= RequestMethod.GET)
	public String index() {
		return "user/invitation/index";
	}
}
