package pl.codecity.main.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import pl.codecity.main.configuration.MyCmsCacheConfiguration;
import pl.codecity.main.exception.ServiceException;
import pl.codecity.main.model.Category;
import pl.codecity.main.model.Category_;
import pl.codecity.main.repository.CategoryRepository;
import pl.codecity.main.repository.CategorySpecifications;
import pl.codecity.main.request.CategoryCreateRequest;
import pl.codecity.main.request.CategorySearchRequest;
import pl.codecity.main.request.CategoryUpdateRequest;
import pl.codecity.main.utility.AuthorizedUser;
import pl.codecity.main.utility.CodeFormatter;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor=Exception.class)
public class CategoryService {

	@Inject
	private CategoryRepository categoryRepository;

	@CacheEvict(value = {MyCmsCacheConfiguration.ARTICLE_CACHE, MyCmsCacheConfiguration.PAGE_CACHE}, allEntries = true)
	public Category createCategory(CategoryCreateRequest request, AuthorizedUser authorizedUser) {
		Category category = new Category();

		Category parent = null;
		if (request.getParentId() != null) {
			parent = categoryRepository.findOneByIdAndLanguage(request.getParentId(), request.getLanguage());
		}

		int rgt = 0;
		if (parent == null) {
			rgt = categoryRepository.findMaxRgt();
			rgt++;
		}
		else {
			rgt = parent.getRgt();
			categoryRepository.unshiftRgt(rgt);
			categoryRepository.unshiftLft(rgt);
		}

		category.setParent(parent);
		String code = request.getCode();
		if (code == null) {
			try {
				code = new CodeFormatter().parse(request.getName(), LocaleContextHolder.getLocale());
			} catch (ParseException e) {
				throw new ServiceException(e);
			}
		}
		category.setCode(code);
		category.setName(request.getName());
		category.setDescription(request.getDescription());
		category.setLft(rgt);
		category.setRgt(rgt + 1);
		category.setLanguage(request.getLanguage());

		return categoryRepository.save(category);
	}

	@CacheEvict(value = {MyCmsCacheConfiguration.ARTICLE_CACHE, MyCmsCacheConfiguration.PAGE_CACHE}, allEntries = true)
	public Category updateCategory(CategoryUpdateRequest request, AuthorizedUser authorizedUser) {
		categoryRepository.lock(request.getId());
		Category category = categoryRepository.findOneByIdAndLanguage(request.getId(), request.getLanguage());
		Category parent = null;
		if (request.getParentId() != null) {
			parent = categoryRepository.findOneByIdAndLanguage(request.getParentId(), request.getLanguage());
		}

		if (!(category.getParent() == null && parent == null) && !ObjectUtils.nullSafeEquals(category.getParent(), parent)) {
			categoryRepository.shiftLftRgt(category.getLft(), category.getRgt());
			categoryRepository.shiftRgt(category.getRgt());
			categoryRepository.shiftLft(category.getRgt());

			int rgt = 0;
			if (parent == null) {
				rgt = categoryRepository.findMaxRgt();
				rgt++;
			}
			else {
				rgt = parent.getRgt();
				categoryRepository.unshiftRgt(rgt);
				categoryRepository.unshiftLft(rgt);
			}
			category.setLft(rgt);
			category.setRgt(rgt + 1);
		}

		category.setParent(parent);
		String code = request.getCode();
		if (code == null) {
			try {
				code = new CodeFormatter().parse(request.getName(), LocaleContextHolder.getLocale());
			} catch (ParseException e) {
				throw new ServiceException(e);
			}
		}
		category.setCode(code);
		category.setName(request.getName());
		category.setDescription(request.getDescription());
		category.setLanguage(request.getLanguage());

		return categoryRepository.save(category);
	}

	@CacheEvict(value = {MyCmsCacheConfiguration.ARTICLE_CACHE, MyCmsCacheConfiguration.PAGE_CACHE}, allEntries = true)
	public void updateCategoryHierarchy(List<Map<String, Object>> data, String language) {
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> map = data.get(i);
			if (map.get("item_id") != null) {
				categoryRepository.lock(Long.parseLong((String) map.get("item_id")));
				Category category = categoryRepository.findOneByIdAndLanguage(Long.parseLong((String) map.get("item_id")), language);
				if (category != null) {
					Category parent = null;
					if (map.get("parent_id") != null) {
						parent = categoryRepository.findOneByIdAndLanguage(Long.parseLong((String) map.get("parent_id")), language);
					}
					category.setParent(parent);
					category.setLft(((int) map.get("left")) - 1);
					category.setRgt(((int) map.get("right")) - 1);
					categoryRepository.save(category);
				}
			}
		}
	}

	@CacheEvict(value = {MyCmsCacheConfiguration.ARTICLE_CACHE, MyCmsCacheConfiguration.PAGE_CACHE}, allEntries = true)
	public Category deleteCategory(long id, String language) {
		categoryRepository.lock(id);
		Category category = categoryRepository.findOneByIdAndLanguage(id, language);
		Category parent = category.getParent();
		for (Category child : category.getChildren()) {
			child.setParent(parent);
			categoryRepository.saveAndFlush(child);
		}
		category.getChildren().clear();
		categoryRepository.saveAndFlush(category);
		categoryRepository.delete(category);

		categoryRepository.shiftLftRgt(category.getLft(), category.getRgt());
		categoryRepository.shiftRgt(category.getRgt());
		categoryRepository.shiftLft(category.getRgt());

		return category;
	}

	public Category getCategoryById(long id, String language) {
		return categoryRepository.findOneByIdAndLanguage(id, language);
	}

	public Category getCategoryByCode(String code, String language) {
		return categoryRepository.findOneByCodeAndLanguage(code, language);
	}

	public List<Category> getCategories(String language) {
		return getCategories(language, false);
	}

	public List<Category> getCategories(String language, boolean includeNoPosts) {
		if (includeNoPosts) {
			return categoryRepository.findAllDistinctByLanguageOrderByLftAsc(language);
		} else {
			return categoryRepository.findAll(CategorySpecifications.hasPosts(language), new Sort(Category_.lft.getName()));
		}
	}

	public Page<Category> getCategories(CategorySearchRequest request) {
		Pageable pageable = new PageRequest(0, 10);
		return getCategories(request, pageable);
	}

	public Page<Category> getCategories(CategorySearchRequest request, Pageable pageable) {
		return categoryRepository.search(request, pageable);
	}
}
