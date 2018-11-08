package pl.codecity.main.controller.admin.customfield;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import pl.codecity.main.request.CustomFieldSearchRequest;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CustomFieldSearchForm implements Serializable {
	
	private String keyword;

	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public boolean isEmpty() {
		if (StringUtils.hasText(getKeyword())) {
			return false;
		}
		return true;
	}
	
	public boolean isAdvanced() {
		return false;
	}
	
	public CustomFieldSearchRequest toCustomFieldSearchRequest() {
		CustomFieldSearchRequest request = new CustomFieldSearchRequest();
		request.setKeyword(getKeyword());
		request.setLanguage(LocaleContextHolder.getLocale().getLanguage());
		return request;
	}

	public MultiValueMap<String, String> toQueryParams() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		if (StringUtils.hasText(keyword)) {
			params.add("keyword", keyword);
		}
		return params;
	}

}
