package pl.codecity.main.request;

import pl.codecity.main.model.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class PostSearchRequest {

	private Collection<Long> postIds = new ArrayList<>();
	private Post.Status status = Post.Status.PUBLISHED;
	private String keyword;
	private Collection<String> categoryCodes = new ArrayList<>();
	private Collection<String> tagNames = new ArrayList<>();
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private String language;

	public PostSearchRequest(String language) {
		this.language = language;
	}

	public Collection<Long> getPostIds() {
		return postIds;
	}

	public void setPostIds(Collection<Long> postIds) {
		this.postIds = postIds;
	}

	public PostSearchRequest withPostIds(Long... postIds) {
		if (getPostIds() == null) {
			setPostIds(new ArrayList<Long>(postIds.length));
		}
		for (Long value : postIds) {
			getPostIds().add(value);
		}
		return this;
	}

	public Post.Status getStatus() {
		return status;
	}

	public void setStatus(Post.Status status) {
		this.status = status;
	}
	
	public PostSearchRequest withStatus(Post.Status status) {
		this.status = status;
		return this;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public PostSearchRequest withKeyword(String keyword) {
		this.keyword = keyword;
		return this;
	}

	public Collection<String> getCategoryCodes() {
		return categoryCodes;
	}

	public void setCategoryCodes(Collection<String> categoryCodes) {
		this.categoryCodes = categoryCodes;
	}

	public PostSearchRequest withCategoryCodes(String... categoryCodes) {
		if (getCategoryCodes() == null) {
			setCategoryCodes(new ArrayList<String>(categoryCodes.length));
		}
		for (String value : categoryCodes) {
			getCategoryCodes().add(value);
		}
		return this;
	}

	public Collection<String> getTagNames() {
		return tagNames;
	}

	public void setTagNames(Collection<String> tagNames) {
		this.tagNames = tagNames;
	}

	public PostSearchRequest withTagNames(String... tagNames) {
		if (getTagNames() == null) {
			setTagNames(new ArrayList<String>(tagNames.length));
		}
		for (String value : tagNames) {
			getTagNames().add(value);
		}
		return this;
	}

	public LocalDateTime getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(LocalDateTime dateFrom) {
		this.dateFrom = dateFrom;
	}

	public PostSearchRequest withDateFrom(LocalDateTime dateFrom) {
		this.dateFrom = dateFrom;
		return this;
	}

	public LocalDateTime getDateTo() {
		return dateTo;
	}

	public void setDateTo(LocalDateTime dateTo) {
		this.dateTo = dateTo;
	}

	public PostSearchRequest withDateTo(LocalDateTime dateTo) {
		this.dateTo = dateTo;
		return this;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public PostSearchRequest withLanguage(String language) {
		this.language = language;
		return this;
	}
}
