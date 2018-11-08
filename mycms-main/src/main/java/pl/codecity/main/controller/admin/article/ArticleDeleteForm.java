package pl.codecity.main.controller.admin.article;

import pl.codecity.main.request.ArticleDeleteRequest;

import javax.validation.constraints.NotNull;

public class ArticleDeleteForm {

	@NotNull
	private Long id;

	@NotNull
	private String language;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ArticleDeleteRequest buildArticleDeleteRequest() {
		ArticleDeleteRequest.Builder builder = new ArticleDeleteRequest.Builder();
		return builder
				.id(id)
				.language(language)
				.build();
	}
}
