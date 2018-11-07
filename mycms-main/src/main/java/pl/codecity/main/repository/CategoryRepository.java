package pl.codecity.main.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.codecity.main.model.Category;

import java.util.List;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

	@EntityGraph(value = Category.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	Category findOneByIdAndLanguage(Long id, String language);

//	@EntityGraph(value = Category.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
//	@Lock(LockModeType.PESSIMISTIC_WRITE)
//	Category findOneForUpdateByIdAndLanguage(Long id, String language);

	@EntityGraph(value = Category.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	Category findOneByCodeAndLanguage(String code, String language);

	@EntityGraph(value = Category.SHALLOW_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	List<Category> findAll(Specification<Category> spec, Sort sort);

	@EntityGraph(value = Category.SHALLOW_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	List<Category> findAllDistinctByLanguageOrderByLftAsc(String language);

	@Query("select coalesce(max(rgt), 0) from Category ")
	int findMaxRgt();

	@Modifying
	@Query("update Category set lft = lft + 2 where lft > :rgt ")
	void unshiftLft(@Param("rgt") int rgt);

	@Modifying
	@Query("update Category set rgt = rgt + 2 where rgt >= :rgt ")
	void unshiftRgt(@Param("rgt") int rgt);

	@Modifying
	@Query("update Category set rgt = rgt - 1, lft = lft - 1 where lft between :lft and :rgt ")
	void shiftLftRgt(@Param("lft") int lft, @Param("rgt") int rgt);

	@Modifying
	@Query("update Category set lft = lft - 2 where lft > :rgt ")
	void shiftLft(@Param("rgt") int rgt);

	@Modifying
	@Query("update Category set rgt = rgt - 2 where rgt > :rgt ")
	void shiftRgt(@Param("rgt") int rgt);
}
