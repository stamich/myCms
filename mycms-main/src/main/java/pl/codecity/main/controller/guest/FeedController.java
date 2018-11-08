package pl.codecity.main.controller.guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.codecity.main.model.Article;
import pl.codecity.main.model.BlogLanguage;
import pl.codecity.main.model.Category;
import pl.codecity.main.model.Post;
import pl.codecity.main.request.ArticleSearchRequest;
import pl.codecity.main.service.ArticleService;
import pl.codecity.main.service.CategoryService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Controller
@RequestMapping("/feed")
public class FeedController {

	private static final PageRequest DEFAULT_PAGE_REQUEST = new PageRequest(0, 50);

	@Inject
	private ArticleService articleService;
	
	@Autowired
	private CategoryService categoryService;

	@RequestMapping("rss.xml")
	public String indexRss(
			BlogLanguage blogLanguage,
			Model model) {
		ArticleSearchRequest request = new ArticleSearchRequest()
				.withStatus(Post.Status.PUBLISHED)
				.withLanguage(blogLanguage.getLanguage());
		Page<Article> articles = articleService.getArticles(request, DEFAULT_PAGE_REQUEST);
		model.addAttribute("articles", new TreeSet<>(articles.getContent()));
		return "rssFeedView";
	}

	@RequestMapping("atom.xml")
	public String indexAtom(
			BlogLanguage blogLanguage,
			Model model) {
		indexRss(blogLanguage, model);
		return "atomFeedView";
	}

	@RequestMapping("category/{categoryCode}/rss.xml")
	public String categoryRss(
			@PathVariable String categoryCode,
			BlogLanguage blogLanguage,
			Model model) {
		Category category = categoryService.getCategoryByCode(categoryCode, blogLanguage.getLanguage());
		List<Long> categoryIds = new ArrayList<>();
		categoryIds.add(category.getId());

		ArticleSearchRequest request = new ArticleSearchRequest()
				.withStatus(Post.Status.PUBLISHED)
				.withLanguage(blogLanguage.getLanguage())
				.withCategoryIds(categoryIds);

		Page<Article> articles = articleService.getArticles(request, DEFAULT_PAGE_REQUEST);
		model.addAttribute("articles", new TreeSet<>(articles.getContent()));
		return "rssFeedView";
	}

	@RequestMapping("category/{categoryCode}/atom.xml")
	public String categoryAtom(
			@PathVariable String categoryCode,
			BlogLanguage blogLanguage,
			Model model) {
		categoryRss(categoryCode, blogLanguage, model);
		return "atomFeedView";
	}
}
