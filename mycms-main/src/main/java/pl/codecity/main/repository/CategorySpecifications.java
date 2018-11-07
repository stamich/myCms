package pl.codecity.main.repository;

import org.springframework.data.jpa.domain.Specification;
import pl.codecity.main.model.*;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class CategorySpecifications {

	public static Specification<Category> hasPosts(String language) {
		return (root, query, cb) -> {
			query.distinct(true);

			Subquery<Long> subquery = query.subquery(Long.class);
			Root<Post> a = subquery.from(Post.class);
			Join<Post, Category> c = a.join(Post_.categories, JoinType.INNER);
			subquery.select(c.get(Category_.id)).where(cb.equal(a.get(Post_.status), Post.Status.PUBLISHED));

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(root.get(Category_.id).in(subquery));
			predicates.add(cb.equal(root.get(Category_.language), language));
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	public static Specification<Category> hasArticles(String language) {
		return (root, query, cb) -> {
			query.distinct(true);

			Subquery<Long> subquery = query.subquery(Long.class);
			Root<Article> a = subquery.from(Article.class);
			Join<Article, Category> c = a.join(Article_.categories, JoinType.INNER);
			subquery.select(c.get(Category_.id)).where(cb.equal(a.get(Article_.status), Article.Status.PUBLISHED));

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(root.get(Category_.id).in(subquery));
			predicates.add(cb.equal(root.get(Category_.language), language));
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	public static Specification<Category> hasPages(String language) {
		return (root, query, cb) -> {
			query.distinct(true);

			Subquery<Long> subquery = query.subquery(Long.class);
			Root<Page> a = subquery.from(Page.class);
			Join<Page, Category> c = a.join(Page_.categories, JoinType.INNER);
			subquery.select(c.get(Category_.id)).where(cb.equal(a.get(Page_.status), Page.Status.PUBLISHED));

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(root.get(Category_.id).in(subquery));
			predicates.add(cb.equal(root.get(Category_.language), language));
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
