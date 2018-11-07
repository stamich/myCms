package pl.codecity.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wallride.domain.Comment;
import org.wallride.model.CommentSearchRequest;
import pl.codecity.main.model.Comment;

public interface CommentRepositoryCustom {

	Page<Comment> search(CommentSearchRequest request, Pageable pageable);
}
