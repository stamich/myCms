package pl.codecity.main.utility;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.codecity.main.model.Article;
import pl.codecity.main.request.ArticleSearchRequest;
import pl.codecity.main.service.ArticleService;

public class ArticleUtils {

	private ArticleService articleService;

	public ArticleUtils(ArticleService articleService) {
		this.articleService = articleService;
	}

	public Page<Article> search(ArticleSearchRequest request, int size) {
		return articleService.getArticles(request, new PageRequest(0, size));
	}
}
