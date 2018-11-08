package pl.codecity.main.controller.admin.article;

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
import pl.codecity.main.model.Article;
import pl.codecity.main.model.Category;
import pl.codecity.main.model.CustomField;
import pl.codecity.main.model.Post;
import pl.codecity.main.request.TreeNode;
import pl.codecity.main.service.ArticleService;
import pl.codecity.main.service.CustomFieldService;
import pl.codecity.main.utility.AuthorizedUser;
import pl.codecity.main.utility.CategoryUtils;

import javax.inject.Inject;
import javax.validation.groups.Default;
import java.util.List;
import java.util.SortedSet;

@Controller
@RequestMapping("/{language}/articles/create")
public class ArticleCreateController {

	private static Logger logger = LoggerFactory.getLogger(ArticleCreateController.class);

	@Inject
	private ArticleService articleService;

	@Inject
	private CategoryUtils categoryUtils;

	@Inject
	private CustomFieldService customFieldService;

	@Inject
	private MessageSourceAccessor messageSourceAccessor;

	@ModelAttribute("form")
	public ArticleCreateForm articleCreateForm(
			@PathVariable String language,
			@RequestParam(required = false) String code) {
		SortedSet<CustomField> customFields = customFieldService.getAllCustomFields(language);
		ArticleCreateForm form = new ArticleCreateForm(customFields);
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
	public @ResponseBody
	RestValidationErrorModel bindException(BindException e) {
		logger.debug("BindException", e);
		return RestValidationErrorModel.fromBindingResult(e.getBindingResult(), messageSourceAccessor);
	}

	@RequestMapping(method=RequestMethod.GET)
	public String create() {
		return "article/create";
	}

	@RequestMapping(method=RequestMethod.GET, params="part=category-fieldset")
	public String partCategoryFieldset(@PathVariable String language) {
		return "article/create::#category-fieldset";
	}

	@RequestMapping(method=RequestMethod.POST, params="draft")
	public @ResponseBody
	DomainObjectSavedModel saveAsDraft(
			@PathVariable String language,
			@Validated @ModelAttribute("form") ArticleCreateForm form,
			BindingResult errors,
			AuthorizedUser authorizedUser)
			throws BindException {
		if (errors.hasErrors()) {
			for (ObjectError error : errors.getAllErrors()) {
				if (!"validation.NotNull".equals(error.getCode())) {
					throw new BindException(errors);
				}
			}
		}

		Article article = null;
		try {
			article = articleService.createArticle(form.buildArticleCreateRequest(), Post.Status.DRAFT, authorizedUser);
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

		return new DomainObjectSavedModel<>(article);
	}

	@RequestMapping(method=RequestMethod.POST, params="publish")
	public String saveAsPublished(
			@PathVariable String language,
			@Validated({Default.class, ArticleCreateForm.GroupPublish.class}) @ModelAttribute("form") ArticleCreateForm form,
			BindingResult errors,
			String query,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes,
			Model model) {
		if (errors.hasErrors()) {
			return "article/create";
		}

		Article article = null;
		try {
			article = articleService.createArticle(form.buildArticleCreateRequest(), Post.Status.PUBLISHED, authorizedUser);
		}
		catch (EmptyCodeException e) {
			errors.rejectValue("code", "NotNull");
		}
		catch (DuplicateCodeException e) {
			errors.rejectValue("code", "NotDuplicate");
		}
		if (errors.hasErrors()) {
			logger.debug("Errors: {}", errors);
			return "article/create";
		}

		redirectAttributes.addFlashAttribute("savedArticle", article);
		redirectAttributes.addAttribute("language", language);
		redirectAttributes.addAttribute("id", article.getId());
		redirectAttributes.addAttribute("query", query);
		return "redirect:/_admin/{language}/articles/describe";
	}

//	@RequestMapping(method=RequestMethod.POST, params="cancel")
//	public String cancel(
//			@PathVariable String language,
//			@Valid @ModelAttribute("form") ArticleCreateForm form,
//			RedirectAttributes redirectAttributes) {
//		redirectAttributes.addAttribute("language", language);
//		return "redirect:/_admin/{language}/articles/";
//	}
}