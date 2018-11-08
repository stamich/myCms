package pl.codecity.main.controller.guest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.codecity.main.controller.support.Pagination;
import pl.codecity.main.model.Article;
import pl.codecity.main.model.BlogLanguage;
import pl.codecity.main.service.ArticleService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class IndexController {

	@Inject
	private ArticleService articleService;

	@RequestMapping
	public String index(
			@PageableDefault(10) Pageable pageable,
			BlogLanguage blogLanguage,
			Model model,
			HttpServletRequest servletRequest) {
		ArticleSearchForm form = new ArticleSearchForm() {};
		form.setLanguage(blogLanguage.getLanguage());

		Page<Article> articles = articleService.getArticles(form.toArticleSearchRequest(), pageable);
		model.addAttribute("articles", articles);
		model.addAttribute("pageable", pageable);
		model.addAttribute("pagination", new Pagination<>(articles, servletRequest));
		return "index";
//
//
//		Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
//		String defaultLanguage = blog.getDefaultLanguage();
//		redirectAttributes.addAttribute("language", defaultLanguage);
//		return "redirect:/{language}/";
	}
}
