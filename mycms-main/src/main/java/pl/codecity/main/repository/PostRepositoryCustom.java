package pl.codecity.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.codecity.main.model.Post;
import pl.codecity.main.request.PostSearchRequest;

public interface PostRepositoryCustom {

	void lock(long id);

	Page<Post> search(PostSearchRequest request, Pageable pageable);
}
