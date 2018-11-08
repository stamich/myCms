package pl.codecity.main.controller.support;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.thymeleaf.context.IExpressionContext;
import pl.codecity.main.model.Page;
import pl.codecity.main.model.Post;
import pl.codecity.main.request.PageSearchRequest;
import pl.codecity.main.request.TreeNode;
import pl.codecity.main.utility.PageUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Pages {

	private IExpressionContext context;

	private PageUtils pageUtils;

	public Pages(IExpressionContext context, PageUtils pageUtils) {
		this.context = context;
		this.pageUtils = pageUtils;
	}

	public List<Page> getAllPages() {
		return pageUtils.getAllPages();
	}

	public List<Page> getAllPages(boolean includeUnpublished) {
		return pageUtils.getAllPages(includeUnpublished);
	}

	public List<TreeNode<Page>> getNodes() {
		return pageUtils.getNodes();
	}

	public List<TreeNode<Page>> getNodes(boolean includeUnpublished) {
		return pageUtils.getNodes(includeUnpublished);
	}

	public Map<Page, String> getPaths(Page page) {
		return pageUtils.getPaths(page);
	}

	public List<Page> getChildren(Page page) {
		return pageUtils.getChildren(page);
	}

	public List<Page> getSiblings(Page page) {
		return pageUtils.getSiblings(page);
	}

	public List<Page> search(Condition condition) {
		org.springframework.data.domain.Page<Page> result = pageUtils.search(condition.buildPageSearchRequest(), condition.size);
		return new ArrayList<>(result.getContent());
	}

	public Condition condition() {
		return new Condition();
	}

	class Condition {

		private int size = 1;
		private String keyword;
		private Collection<Long> categoryIds;
		private Collection<String> categoryCodes;
		private Collection<Long> tagIds;
		private Collection<String> tagNames;
		private MultiValueMap<String, Object> customFields;
		private Long authorId;
		private Post.Status status = Post.Status.PUBLISHED;

		public Condition size(int size) {
			this.size = size;
			return this;
		}

		public Condition keyword(String keyword) {
			this.keyword = keyword;
			return this;
		}

		public Condition category(Long... ids) {
			List<Long> categoryIds = new ArrayList<>();
			for (Long value : ids) {
				categoryIds.add(value);
			}
			this.categoryIds = categoryIds;
			return this;
		}

		public Condition category(String... codes) {
			List<String> categoryCodes = new ArrayList<>();
			for (String value : codes) {
				categoryCodes.add(value);
			}
			this.categoryCodes = categoryCodes;
			return this;
		}

		public Condition tag(String... names) {
			List<String> tagNames = new ArrayList<>();
			for (String value : names) {
				tagNames.add(value);
			}
			this.tagNames = tagNames;
			return this;
		}

		public Condition tag(Long... ids) {
			List<Long> tagIds = new ArrayList<>();
			for (Long value : ids) {
				tagIds.add(value);
			}
			this.tagIds = tagIds;
			return this;
		}

		public Condition customField(String key, Object... values) {
			MultiValueMap<String, Object> customFields = new LinkedMultiValueMap<>();
			for (Object value : values) {
				customFields.add(key, value);
			}
			this.customFields = customFields;
			return this;
		}

		public Condition author(Long id) {
			this.authorId = id;
			return this;
		}

		private PageSearchRequest buildPageSearchRequest() {
			PageSearchRequest request = new PageSearchRequest(context.getLocale().getLanguage())
					.withKeyword(this.keyword)
					.withCategoryIds(this.categoryIds)
					.withCategoryCodes(this.categoryCodes)
					.withTagIds(this.tagIds)
					.withTagNames(this.tagNames)
					.withCustomFields(this.customFields)
					.withAuthorId(this.authorId)
					.withStatus(this.status);
			return request;
		}
	}
}
