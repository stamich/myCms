package pl.codecity.main.controller.support;

import org.springframework.data.domain.Page;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.thymeleaf.context.IExpressionContext;
import pl.codecity.main.model.Article;
import pl.codecity.main.model.Post;
import pl.codecity.main.request.ArticleSearchRequest;
import pl.codecity.main.utility.ArticleUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Articles {

	private IExpressionContext context;
	private ArticleUtils articleUtils;

	public Articles(IExpressionContext context, ArticleUtils articleUtils) {
		this.context = context;
		this.articleUtils = articleUtils;
	}

	public List<Article> search(Condition condition) {
		Page<Article> result = articleUtils.search(condition.buildArticleSearchRequest(), condition.size);
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
		private Collection<String> tagNames;
		private MultiValueMap<String, Object> customFields;
		private Long authorId;
		private Post.Status status = Post.Status.PUBLISHED;
//		private LocalDateTime dateFrom;
//		private LocalDateTime dateTo;

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

		private ArticleSearchRequest buildArticleSearchRequest() {
			ArticleSearchRequest request = new ArticleSearchRequest(context.getLocale().getLanguage())
					.withStatus(this.status)
					.withKeyword(this.keyword)
					.withCategoryIds(this.categoryIds)
					.withCategoryCodes(this.categoryCodes)
					.withTagNames(this.tagNames)
					.withCustomFields(this.customFields)
					.withAuthorId(this.authorId);
			return request;
		}
	}
}