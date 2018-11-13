package pl.codecity.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.codecity.main.model.Comment;
import pl.codecity.main.request.CommentSearchRequest;

public interface CommentRepositoryCustom {

	Page<Comment> search(CommentSearchRequest request, Pageable pageable);
}
