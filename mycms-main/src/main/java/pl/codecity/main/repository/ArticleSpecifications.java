package pl.codecity.main.repository;

import org.springframework.data.jpa.domain.Specification;
import pl.codecity.main.model.Article;
import pl.codecity.main.model.Article_;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;

public class ArticleSpecifications {

	public static Specification<Article> draft(Article article) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.equal(root.get(Article_.drafted), article));

			Subquery<Long> subquery = query.subquery(Long.class);
			Root<Article> p = subquery.from(Article.class);
			subquery.select(cb.max(p.get(Article_.id))).where(cb.equal(p.get(Article_.drafted), article));

			predicates.add(cb.equal(root.get(Article_.id), subquery));
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
