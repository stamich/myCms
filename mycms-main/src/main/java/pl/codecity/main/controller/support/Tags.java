package pl.codecity.main.controller.support;

import org.thymeleaf.context.IExpressionContext;
import pl.codecity.main.model.Tag;
import pl.codecity.main.utility.TagUtils;

import java.util.List;

public class Tags {

	private IExpressionContext context;

	private TagUtils tagUtils;

	public Tags(IExpressionContext context, TagUtils TagUtils) {
		this.context = context;
		this.tagUtils = TagUtils;
	}

	public List<Tag> getAllTags() {
		return tagUtils.getAllTags();
	}
}
