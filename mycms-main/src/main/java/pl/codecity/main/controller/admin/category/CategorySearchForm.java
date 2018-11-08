package pl.codecity.main.controller.admin.category;

import pl.codecity.main.request.CategorySearchRequest;

import java.io.Serializable;

public class CategorySearchForm implements Serializable {

	private String keyword;
	private String language;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public CategorySearchRequest toCategorySearchRequest() {
		return new CategorySearchRequest()
				.withKeyword(getKeyword())
				.withLanguage(getLanguage());
	}
}
