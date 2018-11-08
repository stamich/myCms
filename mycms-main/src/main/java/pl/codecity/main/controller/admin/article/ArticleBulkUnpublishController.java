package pl.codecity.main.controller.admin.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codecity.main.exception.ServiceException;
import pl.codecity.main.model.Article;
import pl.codecity.main.service.ArticleService;
import pl.codecity.main.utility.AuthorizedUser;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping(value="/{language}/articles/bulk-unpublish", method=RequestMethod.POST)
public class ArticleBulkUnpublishController {

	@Inject
	private ArticleService articleService;

	private static Logger logger = LoggerFactory.getLogger(ArticleBulkUnpublishController.class);

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@RequestMapping
	public String unpublish(
			@Valid @ModelAttribute("form") ArticleBulkUnpublishForm form,
			BindingResult errors,
			String query,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("query", query);

		if (errors.hasErrors()) {
			logger.debug("Errors: {}", errors);
			return "redirect:/_admin/{language}/articles/index";
		}

		Collection<Article> unpublishedArticles;
		try {
			unpublishedArticles = articleService.bulkUnpublishArticle(form.toArticleBulkUnpublishRequest(), authorizedUser);
		} catch (ServiceException e) {
			return "redirect:/_admin/{language}/articles/index";
		}

		redirectAttributes.addFlashAttribute("unpublishedArticles", unpublishedArticles);
		return "redirect:/_admin/{language}/articles/index";
	}
}
