package pl.codecity.main.controller.admin.user;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import pl.codecity.main.controller.support.ControllerUtils;
import pl.codecity.main.controller.support.Pagination;
import pl.codecity.main.model.Post;
import pl.codecity.main.model.User;
import pl.codecity.main.service.ArticleService;
import pl.codecity.main.service.UserService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Controller
@RequestMapping("/{language}/users/index")
public class UserSearchController {

	@Inject
	private UserService userService;

	@Inject
	private ArticleService articleService;

	@Inject
	private ConversionService conversionService;

	@ModelAttribute("articleCounts")
	public Map<Long, Long> articleCounts(@PathVariable String language) {
		return articleService.countArticlesByAuthorIdGrouped(Post.Status.PUBLISHED, language);
	}

	@ModelAttribute("form")
	public UserSearchForm setupUserSearchForm() {
		return new UserSearchForm();
	}

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String search(
			@PathVariable String language,
			@Validated @ModelAttribute("form") UserSearchForm form,
			BindingResult result,
			@PageableDefault(50) Pageable pageable,
			Model model,
			HttpServletRequest servletRequest) throws UnsupportedEncodingException {
		Page<User> users = userService.getUsers(form.toUserSearchRequest(), pageable);

		model.addAttribute("users", users);
		model.addAttribute("pageable", pageable);
		model.addAttribute("pagination", new Pagination<>(users, servletRequest));

		UriComponents uriComponents = ServletUriComponentsBuilder
				.fromRequest(servletRequest)
				.queryParams(ControllerUtils.convertBeanForQueryParams(form, conversionService))
				.build();
		if (!StringUtils.isEmpty(uriComponents.getQuery())) {
			model.addAttribute("query", URLDecoder.decode(uriComponents.getQuery(), "UTF-8"));
		}

		return "user/index";
	}

	@RequestMapping(params = "query")
	public String search(
			@PathVariable String language,
			String query,
			Model model,
			SessionStatus sessionStatus,
			RedirectAttributes redirectAttributes) {
		sessionStatus.setComplete();

		for (Map.Entry<String, Object> mapEntry : model.asMap().entrySet()) {
			redirectAttributes.addFlashAttribute(mapEntry.getKey(), mapEntry.getValue());
		}
		String url = UriComponentsBuilder.fromPath("/_admin/{language}/users/index").query(query).buildAndExpand(language).encode().toUriString();
		return "redirect:" + url;
	}

	@RequestMapping(method = RequestMethod.GET, params="part=bulk-delete-form")
	public String partBulkDeleteDialog() {
		return "user/index::bulk-delete-form";
	}
}