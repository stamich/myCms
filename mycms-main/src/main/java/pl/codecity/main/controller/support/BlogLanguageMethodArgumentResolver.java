package pl.codecity.main.controller.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pl.codecity.main.model.Blog;
import pl.codecity.main.model.BlogLanguage;
import pl.codecity.main.service.BlogService;

public class BlogLanguageMethodArgumentResolver implements HandlerMethodArgumentResolver {

	private BlogService blogService;

	public static final String BLOG_LANGUAGE_ATTRIBUTE = BlogLanguageMethodArgumentResolver.class.getName() + ".LANGUAGE";

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return BlogLanguage.class.equals(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(
			MethodParameter parameter,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory)
			throws Exception {
		BlogLanguage blogLanguage = (BlogLanguage) webRequest.getAttribute(BLOG_LANGUAGE_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		if (blogLanguage != null) {
			return blogLanguage;
		}
		Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
		return blog.getLanguage(blog.getDefaultLanguage());
	}
}
