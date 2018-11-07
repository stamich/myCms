package pl.codecity.main.repository;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.Criteria;
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
import pl.codecity.main.model.User;
import pl.codecity.main.request.UserSearchRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<User> search(UserSearchRequest request) {
		return search(request, Pageable.unpaged());
	}

	@Override
	public Page<User> search(UserSearchRequest request, Pageable pageable) {
		Session session = (Session) entityManager.getDelegate();
		Criteria criteria = session.createCriteria(User.class);

		FullTextQuery persistenceQuery = buildFullTextQuery(request, pageable, criteria);
		int resultSize = persistenceQuery.getResultSize();
		List<User> results = persistenceQuery.getResultList();
		return new PageImpl<>(results, pageable, resultSize);
	}

	@Override
	public List<Long> searchForId(UserSearchRequest request) {
		FullTextQuery persistenceQuery = buildFullTextQuery(request, Pageable.unpaged(), null);
		persistenceQuery.setProjection("id");
		List<Object[]> results = persistenceQuery.getResultList();
		List<Long> nos = results.stream().map(result -> (long) result[0]).collect(Collectors.toList());
		return nos;
	}

	private FullTextQuery buildFullTextQuery(UserSearchRequest request, Pageable pageable, Criteria criteria) {
		FullTextEntityManager fullTextEntityManager =  Search.getFullTextEntityManager(entityManager);
		QueryBuilder qb = fullTextEntityManager.getSearchFactory()
				.buildQueryBuilder()
				.forEntity(User.class)
				.get();

		@SuppressWarnings("rawtypes")
		BooleanJunction<BooleanJunction> junction = qb.bool();
		junction.must(qb.all().createQuery());

		if (StringUtils.hasText(request.getKeyword())) {
			Analyzer analyzer = fullTextEntityManager.getSearchFactory().getAnalyzer("synonyms");
			String[] fields = new String[] {
					"loginId",
					"name.firstName", "name.lastName",
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

		if (!CollectionUtils.isEmpty(request.getRoles())) {
			for (User.Role role : request.getRoles()) {
				junction.must(qb.keyword().onField("roles").matching(role).createQuery());
			}
		}

		Query searchQuery = junction.createQuery();

		Sort sort = new Sort(new SortField("sortId", SortField.Type.LONG, false));

		FullTextQuery persistenceQuery = fullTextEntityManager
				.createFullTextQuery(searchQuery, User.class)
				.setCriteriaQuery(criteria)
//				.setProjection("id")
				.setSort(sort);
		if (pageable.isPaged()) {
			persistenceQuery.setFirstResult((int) pageable.getOffset());
			persistenceQuery.setMaxResults(pageable.getPageSize());
		}
		return persistenceQuery;
	}
}
