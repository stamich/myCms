package pl.codecity.main.controller.support;

import org.springframework.web.util.UrlPathHelper;
import pl.codecity.main.model.Blog;
import pl.codecity.main.model.BlogLanguage;
import pl.codecity.main.service.BlogService;

import javax.servlet.http.HttpServletRequest;

public class LanguageUrlPathHelper extends UrlPathHelper {

	private BlogService blogService;

	public LanguageUrlPathHelper(BlogService blogService) {
		this.blogService = blogService;
	}

	@Override
	public String getLookupPathForRequest(HttpServletRequest request) {
		Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
		String defaultLanguage = (blog != null) ? blog.getDefaultLanguage() : null;
		if (defaultLanguage != null) {
			String path = super.getLookupPathForRequest(request);
			boolean languagePath = false;
			for (BlogLanguage blogLanguage : blog.getLanguages()) {
				if (path.startsWith("/" + blogLanguage.getLanguage() + "/")) {
					languagePath = true;
					break;
				}
			}
			if (!languagePath) {
				path = "/" + defaultLanguage + path;
			}
			return path;
		}
		else {
			return super.getLookupPathForRequest(request);
		}
	}
}
