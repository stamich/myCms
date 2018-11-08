package pl.codecity.main.controller.admin.page;

import pl.codecity.main.controller.admin.article.CustomFieldValueEditForm;
import pl.codecity.main.utility.CodeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class PagePreviewForm implements Serializable {

	@CodeFormat
	private String code;

	private String coverId;

	private String title;

	private String body;

	private Long authorId;

	private LocalDateTime date;

	private Long parentId;

	private List<CustomFieldValueEditForm> customFieldValues = new ArrayList<>();

	private String language;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public List<CustomFieldValueEditForm> getCustomFieldValues() {
		return customFieldValues;
	}

	public void setCustomFieldValues(List<CustomFieldValueEditForm> customFieldValues) {
		this.customFieldValues = customFieldValues;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
