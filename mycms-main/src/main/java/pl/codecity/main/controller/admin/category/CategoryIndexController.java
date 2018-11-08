package pl.codecity.main.controller.admin.category;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.codecity.main.model.Post;
import pl.codecity.main.service.ArticleService;
import pl.codecity.main.service.CategoryService;
import pl.codecity.main.utility.CategoryUtils;

import javax.inject.Inject;
import java.util.Map;

@Controller
@RequestMapping("/{language}/categories/index")
public class CategoryIndexController {

	@Inject
	private CategoryService categoryService;

	@Inject
	private CategoryUtils categoryUtils;

	@Inject
	private ArticleService articleService;

	@ModelAttribute("form")
	public CategoryCreateForm categoryCreateForm() {
		return new CategoryCreateForm();
	}

	@ModelAttribute("articleCounts")
	public Map<Long, Long> articleCounts(@PathVariable String language) {
		return articleService.countArticlesByCategoryIdGrouped(Post.Status.PUBLISHED, language);
	}

	@RequestMapping
	public String index(@PathVariable String language, Model model) {
		model.addAttribute("categoryNodes", categoryUtils.getNodes(true));
		return "category/index";
	}

	@RequestMapping(params="part=category-create-form")
	public String partCategoryCreateForm(@PathVariable String language, @RequestParam(required = false) Long parentId, Model model) {
		model.addAttribute("parentId", parentId);
		model.addAttribute("categoryNodes", categoryUtils.getNodes(true));
		return "category/index::category-create-form";
	}

	@RequestMapping(params="part=category-edit-form")
	public String partCategoryEditForm(@PathVariable String language, @RequestParam long id, Model model) {
		model.addAttribute("categoryNodes", categoryUtils.getNodes(true));
		model.addAttribute("category", categoryService.getCategoryById(id, language));
		return "category/index::category-edit-form";
	}

	@RequestMapping(params="part=category-delete-form")
	public String partCategoryDeleteForm(@RequestParam long id, Model model) {
		model.addAttribute("targetId", id);
		return "category/index::category-delete-form";
	}
}