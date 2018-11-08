package pl.codecity.main.controller.admin.page;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import pl.codecity.main.model.Post;
import pl.codecity.main.request.PageSearchRequest;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PageSearchForm implements Serializable {
	
	private String keyword;
	private Long categoryId;
	private Long tagId;
	private Long authorId;
	private Post.Status status;

	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Post.Status getStatus() {
		return status;
	}
	
	public void setStatus(Post.Status status) {
		this.status = status;
	}
	
	public boolean isEmpty() {
		if (StringUtils.hasText(getKeyword())) {
			return false;
		}
		if (getTagId() != null) {
			return false;
		}
		if (getStatus() != null) {
			return false;
		}
		return true;
	}
	
	public boolean isAdvanced() {
		return false;
	}

	public PageSearchRequest toPageSearchRequest() {
		PageSearchRequest request = new PageSearchRequest();
		request.withKeyword(getKeyword());
		if (getCategoryId() != null) {
			request.withCategoryIds(getCategoryId());
		}
		if (getTagId() != null) {
			request.withTagIds(getTagId());
		}
		request.withAuthorId(getAuthorId());
		request.withStatus(getStatus());
		request.withLanguage(LocaleContextHolder.getLocale().getLanguage());
		return request;
	}

	public MultiValueMap<String, String> toQueryParams() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		if (StringUtils.hasText(keyword)) {
			params.add("keyword", keyword);
		}
		if (categoryId != null) {
			params.add("categoryId", Long.toString(categoryId));
		}
		if (tagId != null) {
			params.add("tagId", Long.toString(tagId));
		}
		if (authorId != null) {
			params.add("authorId", Long.toString(authorId));
		}
		if (status != null) {
			params.add("status", status.toString());
		}
		return params;
	}
}
