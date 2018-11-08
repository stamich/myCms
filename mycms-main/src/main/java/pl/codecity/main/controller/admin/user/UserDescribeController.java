package pl.codecity.main.controller.admin.user;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.codecity.main.controller.support.HttpNotFoundException;
import pl.codecity.main.model.Post;
import pl.codecity.main.model.User;
import pl.codecity.main.service.ArticleService;
import pl.codecity.main.service.UserService;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/{language}/users/describe", method= RequestMethod.GET)
public class UserDescribeController {

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

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@RequestMapping
	public String describe(
			@PathVariable String language,
			@RequestParam long id,
			String query,
			Model model) {
		User user = userService.getUserById(id);
		if (user == null) {
			throw new HttpNotFoundException();
		}

		MutablePropertyValues mpvs = new MutablePropertyValues(UriComponentsBuilder.newInstance().query(query).build().getQueryParams());
		for (Iterator<PropertyValue> i = mpvs.getPropertyValueList().iterator(); i.hasNext(); ) {
			PropertyValue pv = i.next();
			boolean hasValue = false;
			for (String value : (List<String>) pv.getValue()) {
				if (StringUtils.hasText(value)) {
					hasValue = true;
					break;
				}
			}
			if (!hasValue) {
				i.remove();
			}
		}
		BeanWrapperImpl beanWrapper = new BeanWrapperImpl(new UserSearchForm());
		beanWrapper.setConversionService(conversionService);
		beanWrapper.setPropertyValues(mpvs, true, true);
		UserSearchForm form = (UserSearchForm) beanWrapper.getWrappedInstance();
		List<Long> ids = userService.getUserIds(form.toUserSearchRequest());
		if (!CollectionUtils.isEmpty(ids)) {
			int index = ids.indexOf(user.getId());
			if (index < ids.size() - 1) {
				Long next = ids.get(index + 1);
				model.addAttribute("next", next);
			}
			if (index > 0) {
				Long prev = ids.get(index - 1);
				model.addAttribute("prev", prev);
			}
		}

		model.addAttribute("user", user);
		model.addAttribute("query", query);
		return "user/describe";
	}
}