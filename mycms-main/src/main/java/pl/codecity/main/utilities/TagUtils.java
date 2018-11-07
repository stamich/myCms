package pl.codecity.main.utilities;

import org.springframework.context.i18n.LocaleContextHolder;
import pl.codecity.main.model.Tag;
import pl.codecity.main.service.TagService;

import java.util.List;

public class TagUtils {

	private TagService tagService;

	public TagUtils(TagService tagService) {
		this.tagService = tagService;
	}

	public List<Tag> getAllTags() {
		return tagService.getTags(LocaleContextHolder.getLocale().getLanguage());
	}
}
