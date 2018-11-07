package pl.codecity.main.request;

import java.io.Serializable;
import java.util.List;

public class CommentBulkDeleteRequest implements Serializable {

	private List<Long> ids;
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
}
