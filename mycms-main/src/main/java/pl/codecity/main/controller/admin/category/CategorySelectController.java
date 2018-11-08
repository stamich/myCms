package pl.codecity.main.controller.admin.category;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.codecity.main.controller.support.DomainObjectSelect2Model;
import pl.codecity.main.model.Category;
import pl.codecity.main.service.CategoryService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CategorySelectController {

	@Inject
	private CategoryService categoryService;

	@RequestMapping(value="/{language}/categories/select")
	public @ResponseBody List<DomainObjectSelect2Model> select(
			@PathVariable String language,
			@RequestParam(required=false) String keyword) {
		CategorySearchForm form = new CategorySearchForm();
		form.setKeyword(keyword);
		form.setLanguage(language);
		Page<Category> categories = categoryService.getCategories(form.toCategorySearchRequest());

		List<DomainObjectSelect2Model> results = new ArrayList<>();
		if (categories.hasContent()) {
			for (Category category : categories) {
				DomainObjectSelect2Model model = new DomainObjectSelect2Model(category.getId(), category.getName());
				results.add(model);
			}
		}
		return results;
	}

	@RequestMapping(value="/{language}/categories/select/{id}", method= RequestMethod.GET)
	public @ResponseBody
	DomainObjectSelect2Model select(
			@PathVariable String language,
			@PathVariable Long id,
			HttpServletResponse response) throws IOException {
		Category category = categoryService.getCategoryById(id, language);
		if (category == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		DomainObjectSelect2Model model = new DomainObjectSelect2Model(category.getId(), category.getName());
		return model;
	}
}
