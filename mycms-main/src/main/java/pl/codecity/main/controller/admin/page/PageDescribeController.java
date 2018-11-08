package pl.codecity.main.controller.admin.page;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import pl.codecity.main.controller.support.HttpNotFoundException;
import pl.codecity.main.service.PageService;
import pl.codecity.main.model.Page;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping(value="/{language}/pages/describe", method=RequestMethod.GET)
public class PageDescribeController {

	@Inject
	private PageService pageService;

	@Inject
	private ConversionService conversionService;

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@RequestMapping
	public String describe(
			@PathVariable String language,
			@RequestParam long id,
			String query,
			Model model,
			RedirectAttributes redirectAttributes) {
		Page page = pageService.getPageById(id);
		if (page == null) {
			throw new HttpNotFoundException();
		}

		if (!page.getLanguage().equals(language)) {
			Page target = pageService.getPageByCode(page.getCode(), language);
			if (target != null) {
				redirectAttributes.addAttribute("id", target.getId());
				return "redirect:/_admin/{language}/pages/describe?id={id}";
			} else {
				redirectAttributes.addFlashAttribute("original", page);
				redirectAttributes.addAttribute("code", page.getCode());
				return "redirect:/_admin/{language}/pages/create?code={code}";
			}
		}

		MutablePropertyValues mpvs = new MutablePropertyValues(UriComponentsBuilder.newInstance().query(query).build().getQueryParams());
		for (Iterator<PropertyValue> i = mpvs.getPropertyValueList().iterator(); i.hasNext();) {
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
		BeanWrapperImpl beanWrapper = new BeanWrapperImpl(new PageSearchForm());
		beanWrapper.setConversionService(conversionService);
		beanWrapper.setPropertyValues(mpvs, true, true);
		PageSearchForm form = (PageSearchForm) beanWrapper.getWrappedInstance();
		List<Long> ids = pageService.getPageIds(form.toPageSearchRequest());
		if (!CollectionUtils.isEmpty(ids)) {
			int index = ids.indexOf(page.getId());
			if (index < ids.size() - 1) {
				Long next = ids.get(index + 1);
				model.addAttribute("next", next);
			}
			if (index > 0) {
				Long prev = ids.get(index -1);
				model.addAttribute("prev", prev);
			}
		}

		model.addAttribute("page", page);
		model.addAttribute("query", query);
		return "page/describe";
	}

	@RequestMapping(params="part=delete-form")
	public String partDeleteDialog(
			@PathVariable String language,
			@RequestParam long id, Model model) {
		Page page = pageService.getPageById(id, language);
		model.addAttribute("page", page);
		return "page/describe::delete-form";
	}
}