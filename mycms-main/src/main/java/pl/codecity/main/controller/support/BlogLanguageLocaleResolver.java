package pl.codecity.main.controller.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.LocaleResolver;
import pl.codecity.main.model.Blog;
import pl.codecity.main.model.BlogLanguage;
import pl.codecity.main.service.BlogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class BlogLanguageLocaleResolver implements LocaleResolver {

	private BlogService blogService;

	private static Logger logger = LoggerFactory.getLogger(BlogLanguageLocaleResolver.class);

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		BlogLanguage blogLanguage = (BlogLanguage) request.getAttribute(BlogLanguageMethodArgumentResolver.BLOG_LANGUAGE_ATTRIBUTE);
		if (blogLanguage == null) {
			Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
			blogLanguage = blog.getLanguage(blog.getDefaultLanguage());
		}

		return (blogLanguage != null) ? Locale.forLanguageTag(blogLanguage.getLanguage()) : request.getLocale();
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		throw new UnsupportedOperationException(
				"Cannot change fixed locale - use a different locale resolution strategy");
	}
}
