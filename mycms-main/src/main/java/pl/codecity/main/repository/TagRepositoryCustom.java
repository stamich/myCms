package pl.codecity.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.codecity.main.model.Tag;
import pl.codecity.main.request.TagSearchRequest;

public interface TagRepositoryCustom {

	Page<Tag> search(TagSearchRequest request, Pageable pageable);
}
