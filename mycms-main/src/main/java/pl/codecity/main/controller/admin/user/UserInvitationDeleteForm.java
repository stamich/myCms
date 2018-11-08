package pl.codecity.main.controller.admin.user;

import pl.codecity.main.request.UserInvitationDeleteRequest;

import javax.validation.constraints.NotNull;

public class UserInvitationDeleteForm {

	@NotNull
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserInvitationDeleteRequest buildUserInvitationDeleteRequest() {
		UserInvitationDeleteRequest.Builder builder = new UserInvitationDeleteRequest.Builder();
		return builder
				.token(token)
				.build();
	}
}
