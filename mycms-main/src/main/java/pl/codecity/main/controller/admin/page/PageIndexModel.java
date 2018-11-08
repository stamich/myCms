package pl.codecity.main.controller.admin.page;

import pl.codecity.main.model.Page;
import pl.codecity.main.request.TreeNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PageIndexModel extends ArrayList<Map<String, Object>> {

	public PageIndexModel(List<TreeNode<Page>> nodes) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		for (TreeNode<Page> node : nodes) {
			result.add(createValue(node));
		}
		this.addAll(result);
	}

	private Map<String, Object> createValue(TreeNode<Page> node) {
		Map<String, Object> parent = new LinkedHashMap<>();
		parent.put("id", node.getObject().getId());
		parent.put("code", node.getObject().getCode());
		parent.put("title", node.getObject().getTitle());
//		parent.put("articleCount", page.getArticleCount());

		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		for (TreeNode<Page> child : node.getChildren()) {
			children.add(createValue(child));
		}
		parent.put("children", children);
		return parent;
	}
}
