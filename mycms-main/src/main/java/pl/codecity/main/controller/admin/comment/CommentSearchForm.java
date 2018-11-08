package pl.codecity.main.controller.admin.comment;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import pl.codecity.main.request.CommentSearchRequest;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CommentSearchForm implements Serializable {

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

	public CommentSearchRequest toCommentSearchRequest() {
		CommentSearchRequest request = new CommentSearchRequest();
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
