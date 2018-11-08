package pl.codecity.main.controller.admin.user;

import pl.codecity.main.request.UserInvitationCreateRequest;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserInvitationCreateForm implements Serializable {

	@NotNull
	private String invitees;

	private String message;

	public String getInvitees() {
		return invitees;
	}

	public void setInvitees(String invitees) {
		this.invitees = invitees;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserInvitationCreateRequest buildUserInvitationCreateRequest() {
		UserInvitationCreateRequest.Builder builder = new UserInvitationCreateRequest.Builder();
		return builder
				.invitees(invitees)
				.message(message)
				.build();
	}
}
