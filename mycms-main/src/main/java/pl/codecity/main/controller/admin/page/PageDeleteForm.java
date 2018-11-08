package pl.codecity.main.controller.admin.page;

import pl.codecity.main.request.PageDeleteRequest;

import javax.validation.constraints.NotNull;

public class PageDeleteForm {

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

	public PageDeleteRequest buildPageDeleteRequest() {
		PageDeleteRequest.Builder builder = new PageDeleteRequest.Builder();
		return builder
				.id(id)
				.language(language)
				.build();
	}
}
