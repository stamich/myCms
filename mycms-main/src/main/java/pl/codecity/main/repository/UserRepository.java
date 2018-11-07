package pl.codecity.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.codecity.main.model.User;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
	
//	@EntityGraph(value = User.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	User findOneById(Long id);

//	@EntityGraph(value = User.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	User findOneForUpdateById(Long id);

//	@EntityGraph(value = User.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	User findOneByLoginId(String loginId);

//	@EntityGraph(value = User.DEEP_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	User findOneByEmail(String email);

//	@EntityGraph(value = User.SHALLOW_GRAPH_NAME, type = EntityGraph.EntityGraphType.FETCH)
	List<User> findAllByIdIn(Collection<Long> ids);
	
	@Modifying
	@Query("update User set lastLoginTime = :lastLoginTime where id = :id ")
	int updateLastLoginTime(@Param("id") long id, @Param("lastLoginTime") Date lastLoginTime);
}
