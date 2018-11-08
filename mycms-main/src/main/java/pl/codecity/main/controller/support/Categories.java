package pl.codecity.main.controller.support;

import org.thymeleaf.context.IExpressionContext;
import pl.codecity.main.model.Category;
import pl.codecity.main.request.TreeNode;
import pl.codecity.main.utility.CategoryUtils;

import java.util.List;

public class Categories {

	private IExpressionContext context;

	private CategoryUtils CategoryUtils;

	public Categories(IExpressionContext context, CategoryUtils CategoryUtils) {
		this.context = context;
		this.CategoryUtils = CategoryUtils;
	}

	public List<Category> getAllCategories() {
		return CategoryUtils.getAllCategories();
	}

	public List<Category> getAllCategories(boolean includeNoPosts) {
		return CategoryUtils.getAllCategories(includeNoPosts);
	}

	public List<TreeNode<Category>> getNodes() {
		return CategoryUtils.getNodes();
	}

	public List<TreeNode<Category>> getNodes(boolean includeNoPosts) {
		return CategoryUtils.getNodes(includeNoPosts);
	}
}