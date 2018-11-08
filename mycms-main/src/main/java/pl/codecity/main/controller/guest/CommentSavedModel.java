package pl.codecity.main.controller.guest;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.codecity.main.controller.support.DomainObjectSavedModel;
import pl.codecity.main.model.Comment;
import pl.codecity.main.utility.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class CommentSavedModel extends DomainObjectSavedModel<Long> {

	private String authorName;
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime date;
	private String content;

	public CommentSavedModel(Comment comment) {
		super(comment);
		authorName = comment.getAuthorName();
		date = comment.getDate();
		content = comment.getContent();
	}

	public String getAuthorName() {
		return authorName;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getContent() {
		return content;
	}
}
