package pl.codecity.main.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.codecity.main.model.Page;
import pl.codecity.main.model.Post;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
public interface PageRepository extends JpaRepository<Page, Long>, PageRepositoryCustom {

	@EntityGraph(value = Page.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	Page findOne(Specification<Page> spec);

	@EntityGraph(value = Page.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	Page findOneById(Long id);

	@EntityGraph(value = Page.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	Page findOneByIdAndLanguage(Long id, String language);

	@EntityGraph(value = Page.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	Page findOneByCodeAndLanguage(String code, String language);

	@EntityGraph(value = Page.SHALLOW_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	List<Page> findAll(Specification<Page> spec);

	@EntityGraph(value = Page.SHALLOW_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	List<Page> findAllByIdIn(Collection<Long> ids);

	@Query("select count(page.id) from Page page where page.language = :language and page.drafted is null ")
	long count(@Param("language") String language);

	@Query("select count(page.id) from Page page where page.status = :status and page.language = :language and page.drafted is null ")
	long countByStatus(@Param("status") Post.Status status, @Param("language") String language);

	@Query("select coalesce(max(rgt), 0) from Page ")
	int findMaxRgt();

	@Modifying
	@Query("update Page set lft = lft + 2 where lft > :rgt ")
	void unshiftLft(@Param("rgt") int rgt);

	@Modifying
	@Query("update Page set rgt = rgt + 2 where rgt >= :rgt ")
	void unshiftRgt(@Param("rgt") int rgt);

	@Modifying
	@Query("update Page set rgt = rgt - 1, lft = lft - 1 where lft between :lft and :rgt ")
	void shiftLftRgt(@Param("lft") int lft, @Param("rgt") int rgt);

	@Modifying
	@Query("update Page set lft = lft - 2 where lft > :rgt ")
	void shiftLft(@Param("rgt") int rgt);

	@Modifying
	@Query("update Page set rgt = rgt - 2 where rgt > :rgt ")
	void shiftRgt(@Param("rgt") int rgt);

	@Modifying
	@Query("delete from Page page where page.drafted = :drafted ")
	void deleteByDrafted(@Param("drafted") Page dradted);
}
