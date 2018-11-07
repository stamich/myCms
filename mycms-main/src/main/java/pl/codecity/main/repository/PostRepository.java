package pl.codecity.main.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.codecity.main.model.Post;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
	
	@EntityGraph(value = Post.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	Post findOneByIdAndLanguage(Long id, String language);

	@EntityGraph(value = Post.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	Post findOneByCodeAndLanguage(String code, String language);

	@EntityGraph(value = Post.SHALLOW_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	List<Post> findAllByStatusAndDateLessThanEqual(Post.Status status, LocalDateTime date);

	@Query("select count(post.id) from Post post where post.language = :language ")
	long count(@Param("language") String language);

	@Query("select count(post.id) from Post post where post.status = :status and post.language = :language ")
	long countByStatus(@Param("status") Post.Status status, @Param("language") String language);
}
