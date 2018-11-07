package pl.codecity.main.repository;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pl.codecity.main.model.Post;
import pl.codecity.main.model.Post_;
import pl.codecity.main.request.PostSearchRequest;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class PostRepositoryImpl implements PostRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void lock(long id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<Post> root = query.from(Post.class);
		query.select(root.get(Post_.id));
		query.where(cb.equal(root.get(Post_.id), id));
		entityManager.createQuery(query).setLockMode(LockModeType.PESSIMISTIC_WRITE).getSingleResult();
	}

	@Override
	public Page<Post> search(PostSearchRequest request, Pageable pageable) {
		FullTextEntityManager fullTextEntityManager =  Search.getFullTextEntityManager(entityManager);
		QueryBuilder qb = fullTextEntityManager.getSearchFactory()
				.buildQueryBuilder()
				.forEntity(Post.class)
				.get();

		@SuppressWarnings("rawtypes")
		BooleanJunction<BooleanJunction> junction = qb.bool();
		junction.must(qb.all().createQuery());

		junction.must(qb.keyword().onField("drafted").ignoreAnalyzer().matching("_null_").createQuery());

		if (StringUtils.hasText(request.getKeyword())) {
			Analyzer analyzer = fullTextEntityManager.getSearchFactory().getAnalyzer("synonyms");
			String[] fields = new String[] {
					"title", "body",
					"categories.code",
					"tags.name",
			};
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
			parser.setDefaultOperator(QueryParser.Operator.AND);
			Query query = null;
			try {
				query = parser.parse(request.getKeyword());
			}
			catch (ParseException e1) {
				try {
					query = parser.parse(QueryParser.escape(request.getKeyword()));
				}
				catch (ParseException e2) {
					throw new RuntimeException(e2);
				}
			}
			junction.must(query);
		}
		if (request.getStatus() != null) {
			junction.must(qb.keyword().onField("status").matching(request.getStatus()).createQuery());
		}
		if (StringUtils.hasText(request.getLanguage())) {
			junction.must(qb.keyword().onField("language").matching(request.getLanguage()).createQuery());
		}

		if (request.getDateFrom() != null) {
			junction.must(qb.range().onField("date").above(request.getDateFrom()).createQuery());
		}
		if (request.getDateTo() != null) {
			junction.must(qb.range().onField("date").below(request.getDateTo()).createQuery());
		}

		if (!CollectionUtils.isEmpty(request.getCategoryCodes())) {
			BooleanJunction<BooleanJunction> subJunction = qb.bool();
			for (String categoryCode : request.getCategoryCodes()) {
				subJunction.should(qb.phrase().onField("categories.code").sentence(categoryCode).createQuery());
			}
			junction.must(subJunction.createQuery());
		}

		if (!CollectionUtils.isEmpty(request.getTagNames())) {
			BooleanJunction<BooleanJunction> subJunction = qb.bool();
			for (String tagName : request.getTagNames()) {
				subJunction.should(qb.phrase().onField("tags.name").sentence(tagName).createQuery());
			}
			junction.must(subJunction.createQuery());
		}

		if (!CollectionUtils.isEmpty(request.getPostIds())) {
			BooleanJunction<BooleanJunction> bool = qb.bool();
			for (long postId : request.getPostIds()) {
				bool.should(qb.keyword().onField("id").matching(postId).createQuery());
			}
			junction.must(bool.createQuery());
		}

		Query searchQuery = junction.createQuery();

		Session session = (Session) entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Post.class)
				.setFetchMode("cover", FetchMode.JOIN)
				.setFetchMode("tags", FetchMode.JOIN)
				.setFetchMode("categories", FetchMode.JOIN)
				.setFetchMode("customFieldValues", FetchMode.JOIN)
				.setFetchMode("customFieldValues.customField", FetchMode.JOIN)
				.setFetchMode("author", FetchMode.JOIN);

		Sort sort = new Sort(
				new SortField("sortDate", SortField.Type.STRING, true),
				new SortField("sortId", SortField.Type.LONG, true));

		FullTextQuery persistenceQuery = fullTextEntityManager
				.createFullTextQuery(searchQuery, Post.class)
				.setCriteriaQuery(criteria)
				.setSort(sort);
		persistenceQuery.setFirstResult((int) pageable.getOffset());
		persistenceQuery.setMaxResults(pageable.getPageSize());

		int resultSize = persistenceQuery.getResultSize();

		@SuppressWarnings("unchecked")
		List<Post> results = persistenceQuery.getResultList();
		return new PageImpl<>(results, pageable, resultSize);
	}
}
