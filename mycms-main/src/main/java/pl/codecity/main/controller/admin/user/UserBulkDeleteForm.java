package pl.codecity.main.controller.admin.user;

import pl.codecity.main.request.UserBulkDeleteRequest;

import java.util.List;

public class UserBulkDeleteForm {

	private List<Long> ids;

	private boolean confirmed;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public UserBulkDeleteRequest buildUserBulkDeleteRequest() {
		UserBulkDeleteRequest.Builder builder = new UserBulkDeleteRequest.Builder();
		return builder
				.ids(ids)
				.build();
	}
}
