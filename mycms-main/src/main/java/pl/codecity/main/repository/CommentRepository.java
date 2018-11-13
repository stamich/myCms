package pl.codecity.main.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.codecity.main.model.Comment;

import javax.persistence.LockModeType;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

	@EntityGraph(value = Comment.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	Comment findOneById(Long id);

	@EntityGraph(value = Comment.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Comment findOneForUpdateById(Long id);
}
