package pl.codecity.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wallride.domain.CustomField;
import org.wallride.model.CustomFieldSearchRequest;
import pl.codecity.main.model.CustomField;

import java.util.List;

public interface CustomFieldRepositoryCustom {

	void lock(long id);
	Page<CustomField> search(CustomFieldSearchRequest request);
	Page<CustomField> search(CustomFieldSearchRequest request, Pageable pageable);
	List<Long> searchForId(CustomFieldSearchRequest request);
}
