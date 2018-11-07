package pl.codecity.main.request;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pl.codecity.main.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("serial")
public class UserSearchRequest implements Serializable {
	
	private String keyword;
	private List<User.Role> roles;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public UserSearchRequest withKeyword(String keyword) {
		this.keyword = keyword;
		return this;
	}

	public List<User.Role> getRoles() {
		return roles;
	}

	public void setRoles(List<User.Role> roles) {
		this.roles = roles;
	}

	public UserSearchRequest withRoles(User.Role... roles) {
		if (getRoles() == null) {
			setRoles(new ArrayList<User.Role>(roles.length));
		}
		for (User.Role value : roles) {
			getRoles().add(value);
		}
		return this;
	}

	public UserSearchRequest withRoles(Collection<User.Role> roles) {
		if (roles == null) {
			this.roles = null;
		} else {
			this.roles = new ArrayList<>(roles);
		}
		return this;
	}

	public boolean isEmpty() {
		if (StringUtils.hasText(getKeyword())) {
			return false;
		}
		if (!CollectionUtils.isEmpty(getRoles())) {
			return false;
		}
		return true;
	}
}
