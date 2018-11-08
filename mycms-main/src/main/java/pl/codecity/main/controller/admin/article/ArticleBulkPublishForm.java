package pl.codecity.main.controller.admin.article;

import org.springframework.format.annotation.DateTimeFormat;
import pl.codecity.main.request.ArticleBulkPublishRequest;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class ArticleBulkPublishForm implements Serializable {

	private List<Long> ids;
	@DateTimeFormat(pattern="yyyy/MM/dd HH:mm")
	private LocalDateTime date;
	@NotNull
	private String language;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ArticleBulkPublishRequest toArticleBulkPublishRequest() {
		ArticleBulkPublishRequest request = new ArticleBulkPublishRequest();
		request.setIds(getIds());
		request.setDate(getDate());
		request.setLanguage(getLanguage());
		return request;
	}
}
