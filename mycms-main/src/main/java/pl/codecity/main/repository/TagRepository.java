package pl.codecity.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.codecity.main.model.Tag;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
@Transactional
public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {

	Tag findOneByIdAndLanguage(Long id, String language);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Tag findOneForUpdateByIdAndLanguage(Long id, String language);

	Tag findOneByNameAndLanguage(String name, String language);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Tag findOneForUpdateByNameAndLanguage(String name, String language);

	List<Tag> findAllByLanguage(String language);

	@Query("select count(tag.id) from Tag tag where tag.language = :language ")
	long count(@Param("language") String language);
}
