package pl.codecity.main.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codecity.main.controller.admin.article.ArticleSearchForm;
import pl.codecity.main.model.Article;
import pl.codecity.main.model.Blog;
import pl.codecity.main.model.PopularPost;
import pl.codecity.main.model.Post;
import pl.codecity.main.service.*;

import javax.inject.Inject;
import java.util.List;
import java.util.SortedSet;

@Controller
public class DashboardController {

	@Inject
	private BlogService blogService;
	@Inject
	private PostService postService;
	@Inject
	private ArticleService articleService;
	@Inject
	private PageService pageService;
	@Inject
	private CategoryService categoryService;
	
	@RequestMapping({"/","/dashboard"})
	public String dashboard(RedirectAttributes redirectAttributes) {
		Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
		String defaultLanguage = blog.getDefaultLanguage();
		redirectAttributes.addAttribute("language", defaultLanguage);
		return "redirect:/_admin/{language}/";
	}
	
	@RequestMapping("/{language}/")
	public String dashboard(@PathVariable String language, Model model) {
		long articleCount = articleService.countArticlesByStatus(Post.Status.PUBLISHED, language);
		long pageCount = pageService.countPagesByStatus(Post.Status.PUBLISHED, language);
		long categoryCount = categoryService.getCategories(language).size();

		model.addAttribute("articleCount", articleCount);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("categoryCount", categoryCount);
		model.addAttribute("popularPosts", popularPosts(language));
		model.addAttribute("recentPublishedArticles", recentPublishedArticles(language));
		model.addAttribute("recentDraftArticles", recentDraftArticles(language));

		return "dashboard";
	}

	private SortedSet<PopularPost> popularPosts(String language) {
		return postService.getPopularPosts(language, PopularPost.Type.DAILY);
	}

	private List<Article> recentPublishedArticles(String language) {
		ArticleSearchForm form = new ArticleSearchForm();
		form.setStatus(Post.Status.PUBLISHED);
		Page<Article> page = articleService.getArticles(form.toArticleSearchRequest());
		return page.getContent();
	}

	private List<Article> recentDraftArticles(String language) {
		ArticleSearchForm form = new ArticleSearchForm();
		form.setStatus(Post.Status.DRAFT);
		Page<Article> page = articleService.getArticles(form.toArticleSearchRequest());
		return page.getContent();
	}
}
