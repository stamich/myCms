package pl.codecity.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.codecity.main.model.Category;
import pl.codecity.main.request.CategorySearchRequest;

public interface CategoryRepositoryCustom {

	void lock(long id);
	Page<Category> search(CategorySearchRequest request);
	Page<Category> search(CategorySearchRequest request, Pageable pageable);
}
