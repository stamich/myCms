package pl.codecity.main.repository;

import org.springframework.data.domain.Pageable;
import pl.codecity.main.model.Page;
import pl.codecity.main.request.PageSearchRequest;

import java.util.List;

public interface PageRepositoryCustom {

	org.springframework.data.domain.Page<Page> search(PageSearchRequest request);
	org.springframework.data.domain.Page<Page> search(PageSearchRequest request, Pageable pageable);
	List<Long> searchForId(PageSearchRequest request);
}
