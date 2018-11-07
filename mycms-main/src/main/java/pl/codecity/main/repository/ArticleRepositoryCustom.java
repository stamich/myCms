package pl.codecity.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.codecity.main.model.Article;
import pl.codecity.main.request.ArticleSearchRequest;

import java.util.List;

public interface ArticleRepositoryCustom {

	Page<Article> search(ArticleSearchRequest request);
	Page<Article> search(ArticleSearchRequest request, Pageable pageable);
	List<Long> searchForId(ArticleSearchRequest request);
}
