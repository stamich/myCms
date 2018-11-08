package pl.codecity.main.controller.admin.customfield;

import pl.codecity.main.model.CustomField;
import pl.codecity.main.request.CustomFieldCreateRequest;
import pl.codecity.main.utility.CodeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CustomFieldCreateForm implements Serializable {

	@NotNull
	private String name;

	@CodeFormat
	@NotNull
	private String code;

	@NotNull
	private CustomField.FieldType type = CustomField.FieldType.UNDEFINED;

	private String description;

	private List<String> options = new ArrayList<>();

	@NotNull
	private String language;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public CustomField.FieldType getType() {
		return type;
	}

	public void setType(CustomField.FieldType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public CustomFieldCreateRequest buildCustomFieldCreateRequest() {
		CustomFieldCreateRequest.Builder builder = new CustomFieldCreateRequest.Builder();
		return builder
				.name(name)
				.code(code)
				.description(description)
				.type(type)
				.options(options)
				.language(language)
				.build();
	}
}
