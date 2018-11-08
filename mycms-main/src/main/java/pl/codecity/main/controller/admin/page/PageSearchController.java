package pl.codecity.main.controller.admin.page;

import org.springframework.core.convert.ConversionService;
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
import pl.codecity.main.model.Page;
import pl.codecity.main.model.Post;
import pl.codecity.main.service.PageService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Controller
@RequestMapping("/{language}/pages/index")
public class PageSearchController {
	
	@Inject
	private PageService pageService;

	@Inject
	private ConversionService conversionService;

	@ModelAttribute("countAll")
	public long countAll(@PathVariable String language) {
		return pageService.countPages(language);
	}

	@ModelAttribute("countDraft")
	public long countDraft(@PathVariable String language) {
		return pageService.countPagesByStatus(Post.Status.DRAFT, language);
	}

	@ModelAttribute("countScheduled")
	public long countScheduled(@PathVariable String language) {
		return pageService.countPagesByStatus(Post.Status.SCHEDULED, language);
	}

	@ModelAttribute("countPublished")
	public long countPublished(@PathVariable String language) {
		return pageService.countPagesByStatus(Post.Status.PUBLISHED, language);
	}

	@ModelAttribute("form")
	public PageSearchForm setupPageSearchForm() {
		return new PageSearchForm();
	}

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String search(
			@PathVariable String language,
			@Validated @ModelAttribute("form") PageSearchForm form,
			BindingResult result,
			@PageableDefault(50) Pageable pageable,
			Model model,
			HttpServletRequest servletRequest) throws UnsupportedEncodingException {
		org.springframework.data.domain.Page<Page> pages = pageService.getPages(form.toPageSearchRequest(), pageable);

		model.addAttribute("pages", pages);
		model.addAttribute("pageable", pageable);
		model.addAttribute("pagination", new Pagination<>(pages, servletRequest));

		UriComponents uriComponents = ServletUriComponentsBuilder
				.fromRequest(servletRequest)
				.queryParams(ControllerUtils.convertBeanForQueryParams(form, conversionService))
				.build();
		if (!StringUtils.isEmpty(uriComponents.getQuery())) {
			model.addAttribute("query", URLDecoder.decode(uriComponents.getQuery(), "UTF-8"));
		}

		return "page/index";
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
		String url = UriComponentsBuilder.fromPath("/_admin/{language}/pages/index").query(query).buildAndExpand(language).encode().toUriString();
		return "redirect:" + url;
	}

	@RequestMapping(method = RequestMethod.GET, params = "part=bulk-delete-form")
	public String partBulkDeleteForm(@PathVariable String language) {
		return "page/index::bulk-delete-form";
	}

	@RequestMapping(method = RequestMethod.GET, params = "part=bulk-publish-form")
	public String partBulkPublishForm(@PathVariable String language) {
		return "page/index::bulk-publish-form";
	}

	@RequestMapping(method = RequestMethod.GET, params = "part=bulk-unpublish-form")
	public String partBulkUnpublishForm(@PathVariable String language) {
		return "page/index::bulk-unpublish-form";
	}
}