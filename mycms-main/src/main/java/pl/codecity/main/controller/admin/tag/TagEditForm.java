package pl.codecity.main.controller.admin.tag;

import org.springframework.beans.BeanUtils;
import pl.codecity.main.model.Category;
import pl.codecity.main.request.TagUpdateRequest;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@SuppressWarnings("serial")
public class TagEditForm implements Serializable {

	@NotNull
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private String language;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public TagUpdateRequest buildTagUpdateRequest() {
		TagUpdateRequest.Builder builder = new TagUpdateRequest.Builder();
		return builder
				.id(id)
				.name(name)
				.language(language)
				.build();
	}

	public static TagEditForm fromDomainObject(Category category) {
		TagEditForm form = new TagEditForm();
		BeanUtils.copyProperties(category, form);
		return form;
	}
}
