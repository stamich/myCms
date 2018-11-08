package pl.codecity.main.controller.admin.category;

import pl.codecity.main.model.Category;
import pl.codecity.main.request.TreeNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CategoryIndexModel extends ArrayList<Map<String, Object>> {

	public CategoryIndexModel(List<TreeNode<Category>> nodes) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		for (TreeNode<Category> node : nodes) {
			result.add(createValue(node));
		}
		this.addAll(result);
	}

	private Map<String, Object> createValue(TreeNode<Category> node) {
		Map<String, Object> parent = new LinkedHashMap<>();
		parent.put("id", node.getObject().getId());
		parent.put("code", node.getObject().getCode());
		parent.put("name", node.getObject().getName());
//		parent.put("articleCount", category.getArticleCount());

		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		for (TreeNode<Category> child : node.getChildren()) {
			children.add(createValue(child));
		}
		parent.put("children", children);
		return parent;
	}
}
