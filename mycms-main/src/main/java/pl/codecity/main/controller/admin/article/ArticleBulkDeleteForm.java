package pl.codecity.main.controller.admin.article;

import pl.codecity.main.request.ArticleBulkDeleteRequest;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ArticleBulkDeleteForm {

	private List<Long> ids;

	private boolean confirmed;

	@NotNull
	private String language;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ArticleBulkDeleteRequest buildArticleBulkDeleteRequest() {
		ArticleBulkDeleteRequest.Builder builder = new ArticleBulkDeleteRequest.Builder();
		return builder
				.ids(ids)
				.language(language)
				.build();
	}
}
