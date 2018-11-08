package pl.codecity.main.controller.admin.user;

import org.springframework.util.StringUtils;
import pl.codecity.main.request.UserSearchRequest;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserSearchForm implements Serializable {
	
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

	public UserSearchRequest toUserSearchRequest() {
		return new UserSearchRequest()
				.withKeyword(getKeyword());
	}
}
