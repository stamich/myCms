package pl.codecity.main.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.codecity.main.model.PopularPost;

import java.util.SortedSet;

@Repository
@Transactional
public interface PopularPostRepository extends JpaRepository<PopularPost, Long> {

	SortedSet<PopularPost> findAll(Specification<PopularPost> spec);

	@Modifying
	@Query("delete from PopularPost popularPost where popularPost.language = :language and popularPost.type = :type")
	void deleteByType(@Param("language") String language, @Param("type") PopularPost.Type type);
}
