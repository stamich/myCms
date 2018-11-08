package pl.codecity.main.controller.admin.article;

import org.springframework.format.annotation.DateTimeFormat;
import pl.codecity.main.model.CustomField;
import pl.codecity.main.request.ArticleCreateRequest;
import pl.codecity.main.utility.CodeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings("serial")
public class ArticleCreateForm implements Serializable {

	public ArticleCreateForm() {
	}

	public ArticleCreateForm(SortedSet<CustomField> customFields) {
		for (CustomField field : customFields) {
			CustomFieldValueEditForm valueForm = new CustomFieldValueEditForm();
			valueForm.setCustomFieldId(field.getId());
			valueForm.setName(field.getName());
			valueForm.setDescription(field.getDescription());
			valueForm.setFieldType(field.getFieldType());
			valueForm.setOptions(field.getOptions());
			customFieldValues.add(valueForm);
		}
	}

	interface GroupPublish {}

	@CodeFormat
	private String code;

	private String coverId;

	@NotNull(groups=GroupPublish.class)
	private String title;

	@NotNull(groups=GroupPublish.class)
	private String body;

	private Long authorId;

//	@NotNull
	@DateTimeFormat(pattern="yyyy/MM/dd HH:mm")
	private LocalDateTime date;

	private Set<Long> categoryIds = new HashSet<>();
	private String tags;
	private Set<Long> relatedPostIds = new HashSet<>();

	private String seoTitle;
	private String seoDescription;
	private String seoKeywords;

	@Valid
	private List<CustomFieldValueEditForm> customFieldValues = new ArrayList<>();

	@NotNull
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

	public Set<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(Set<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Set<Long> getRelatedPostIds() {
		return relatedPostIds;
	}

	public void setRelatedPostIds(Set<Long> relatedPostIds) {
		this.relatedPostIds = relatedPostIds;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
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

	public ArticleCreateRequest buildArticleCreateRequest() {
		ArticleCreateRequest.Builder builder = new ArticleCreateRequest.Builder();
		return builder
				.code(code)
				.coverId(coverId)
				.title(title)
				.body(body)
				.authorId(authorId)
				.date(date)
				.categoryIds(categoryIds)
				.tags(tags)
				.relatedPostIds(relatedPostIds)
				.seoTitle(seoTitle)
				.seoDescription(seoDescription)
				.seoKeywords(seoKeywords)
				.customFieldValues(customFieldValues)
				.language(language)
				.build();
	}
}
