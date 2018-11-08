package pl.codecity.main.controller.admin.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codecity.main.controller.support.DomainObjectSavedModel;
import pl.codecity.main.controller.support.RestValidationErrorModel;
import pl.codecity.main.exception.DuplicateCodeException;
import pl.codecity.main.exception.EmptyCodeException;
import pl.codecity.main.model.Category;
import pl.codecity.main.model.CustomField;
import pl.codecity.main.model.Page;
import pl.codecity.main.model.Post;
import pl.codecity.main.request.TreeNode;
import pl.codecity.main.service.CustomFieldService;
import pl.codecity.main.service.PageService;
import pl.codecity.main.utility.AuthorizedUser;
import pl.codecity.main.utility.CategoryUtils;

import javax.inject.Inject;
import javax.validation.groups.Default;
import java.util.List;
import java.util.SortedSet;

@Controller
@RequestMapping("/{language}/pages/create")
public class PageCreateController {

	private static Logger logger = LoggerFactory.getLogger(PageCreateController.class);

	@Inject
	private PageService pageService;

	@Inject
	private CategoryUtils categoryUtils;

	@Inject
	private CustomFieldService customFieldService;

	@Inject
	private MessageSourceAccessor messageSourceAccessor;

	@ModelAttribute("form")
	public PageCreateForm pageCreateForm(
			@PathVariable String language,
			@RequestParam(required = false) String code) {
		SortedSet<CustomField> customFields = customFieldService.getAllCustomFields(language);
		PageCreateForm form = new PageCreateForm(customFields);
		if (code != null) {
			form.setCode(code);
		}
		return form;
	}

	@ModelAttribute("categoryNodes")
	public List<TreeNode<Category>> setupCategoryNodes(@PathVariable String language) {
		return categoryUtils.getNodes(true);
	}

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody RestValidationErrorModel bindException(BindException e) {
		logger.debug("BindException", e);
		return RestValidationErrorModel.fromBindingResult(e.getBindingResult(), messageSourceAccessor);
	}

	@RequestMapping(method=RequestMethod.GET)
	public String create() {
		return "page/create";
	}

	@RequestMapping(method=RequestMethod.GET, params="part=page-fieldset")
	public String partPageFieldset(@PathVariable String language, Model model) {
		return "page/create::#page-fieldset";
	}

	@RequestMapping(method=RequestMethod.GET, params="part=category-fieldset")
	public String partCategoryFieldset(@PathVariable String language) {
		return "page/create::#category-fieldset";
	}

	@RequestMapping(method=RequestMethod.POST, params="draft")
	public @ResponseBody DomainObjectSavedModel saveAsDraft(
			@PathVariable String language,
			@Validated @ModelAttribute("form") PageCreateForm form,
			BindingResult errors,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes)
			throws BindException {
		if (errors.hasErrors()) {
			for (ObjectError error : errors.getAllErrors()) {
				if (!"validation.NotNull".equals(error.getCode())) {
					throw new BindException(errors);
				}
			}
		}

		Page page = null;
		try {
			page = pageService.createPage(form.buildPageCreateRequest(), Post.Status.DRAFT, authorizedUser);
		}
		catch (EmptyCodeException e) {
			errors.rejectValue("code", "NotNull");
		}
		catch (DuplicateCodeException e) {
			errors.rejectValue("code", "NotDuplicate");
		}
		if (errors.hasErrors()) {
			logger.debug("Errors: {}", errors);
			throw new BindException(errors);
		}

		return new DomainObjectSavedModel<>(page);
	}

	@RequestMapping(method=RequestMethod.POST, params="publish")
	public String saveAsPublished(
			@PathVariable String language,
			@Validated({Default.class, PageCreateForm.GroupPublish.class}) @ModelAttribute("form") PageCreateForm form,
			BindingResult errors,
			String query,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			return "page/create";
		}

		Page page = null;
		try {
			page = pageService.createPage(form.buildPageCreateRequest(), Post.Status.PUBLISHED, authorizedUser);
		}
		catch (EmptyCodeException e) {
			errors.rejectValue("code", "NotNull");
		}
		catch (DuplicateCodeException e) {
			errors.rejectValue("code", "NotDuplicate");
		}
		if (errors.hasErrors()) {
			logger.debug("Errors: {}", errors);
			return "page/create";
		}

		redirectAttributes.addFlashAttribute("savedPage", page);
		redirectAttributes.addAttribute("language", language);
		redirectAttributes.addAttribute("id", page.getId());
		redirectAttributes.addAttribute("query", query);
		return "redirect:/_admin/{language}/pages/describe";
	}

//	@RequestMapping(method=RequestMethod.POST, params="cancel")
//	public String cancel(
//			@PathVariable String language,
//			@Valid @ModelAttribute("form") PageCreateForm form,
//			RedirectAttributes redirectAttributes) {
//		redirectAttributes.addAttribute("language", language);
//		return "redirect:/_admin/{language}/pages/";
//	}
}