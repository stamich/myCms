package pl.codecity.main.controller.admin.comment;

import pl.codecity.main.request.CommentBulkUnapproveRequest;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class CommentBulkUnapproveForm implements Serializable {

	private List<Long> ids;
	@NotNull
	private String language;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public CommentBulkUnapproveRequest toCommentBulkUnapproveRequest() {
		CommentBulkUnapproveRequest request = new CommentBulkUnapproveRequest();
		request.setIds(getIds());
		request.setLanguage(getLanguage());
		return request;
	}
}
