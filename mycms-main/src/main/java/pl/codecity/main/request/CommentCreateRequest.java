package pl.codecity.main.request;

import pl.codecity.main.model.BlogLanguage;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CommentCreateRequest implements Serializable {

	private BlogLanguage blogLanguage;
	private long postId;
	private long authorId;
	private LocalDateTime date;
	private String content;
	private boolean approved;

	public BlogLanguage getBlogLanguage() {
		return blogLanguage;
	}

	public void setBlogLanguage(BlogLanguage blogLanguage) {
		this.blogLanguage = blogLanguage;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
}

