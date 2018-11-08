package pl.codecity.main.controller.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.codecity.main.controller.support.DomainObjectSelect2Model;
import pl.codecity.main.model.User;
import pl.codecity.main.service.UserService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserSelect2Controller {

	@Inject
	private UserService userService;

	@RequestMapping(value="/{language}/users/select")
	public @ResponseBody List<DomainObjectSelect2Model> select(
			@PathVariable String language,
			@RequestParam(required=false) String keyword) {
		UserSearchForm form = new UserSearchForm();
		form.setKeyword(keyword);
		Page<User> users = userService.getUsers(form.toUserSearchRequest());

		List<DomainObjectSelect2Model> results = new ArrayList<>();
		if (users.hasContent()) {
			for (User user : users) {
				DomainObjectSelect2Model model = new DomainObjectSelect2Model(user.getId(), user.toString());
				results.add(model);
			}
		}
		return results;
	}

	@RequestMapping(value="/{language}/users/select/{id}", method= RequestMethod.GET)
	public @ResponseBody
	DomainObjectSelect2Model select(
			@PathVariable String language,
			@PathVariable Long id,
			HttpServletResponse response) throws IOException {
		User user = userService.getUserById(id);
		if (user == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		DomainObjectSelect2Model model = new DomainObjectSelect2Model(user.getId(), user.toString());
		return model;
	}
}
