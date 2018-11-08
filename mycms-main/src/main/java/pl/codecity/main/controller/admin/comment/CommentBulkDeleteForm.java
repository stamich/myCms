package pl.codecity.main.controller.admin.comment;

import pl.codecity.main.request.CommentBulkDeleteRequest;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CommentBulkDeleteForm {

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

	public CommentBulkDeleteRequest toCommentBulkDeleteRequest() {
		CommentBulkDeleteRequest request = new CommentBulkDeleteRequest();
		request.setIds(getIds());
		request.setLanguage(getLanguage());
		return request;
	}
}
