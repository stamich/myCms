package pl.codecity.main.controller.admin.tag;

import org.springframework.context.i18n.LocaleContextHolder;
import pl.codecity.main.request.TagSearchRequest;

import java.io.Serializable;

public class TagSearchForm implements Serializable {

	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public TagSearchRequest toTagSearchRequest() {
		TagSearchRequest.Builder builder = new TagSearchRequest.Builder();
		return builder
				.keyword(keyword)
				.language(LocaleContextHolder.getLocale().getLanguage())
				.build();
	}
}
