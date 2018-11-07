package pl.codecity.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.codecity.main.model.User;
import pl.codecity.main.request.UserSearchRequest;

import java.util.List;

public interface UserRepositoryCustom {

	Page<User> search(UserSearchRequest request);
	Page<User> search(UserSearchRequest request, Pageable pageable);
	List<Long> searchForId(UserSearchRequest request);
}
