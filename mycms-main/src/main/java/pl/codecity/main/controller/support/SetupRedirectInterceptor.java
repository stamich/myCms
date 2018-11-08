package pl.codecity.main.controller.support;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.util.UrlPathHelper;
import pl.codecity.main.model.Blog;
import pl.codecity.main.service.BlogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SetupRedirectInterceptor extends HandlerInterceptorAdapter {

    private static final String SETUP_PATH = "/_admin/setup";

	private BlogService blogService;

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
		if (blog != null) {
            return true;
        }

        final String requestPath = getRequestPath(request);
        if (!SETUP_PATH.equalsIgnoreCase(requestPath) && !isResourceHandler(handler)) {
            response.sendRedirect(request.getContextPath() + SETUP_PATH);
            return false;
        }

        return true;
    }

    private String getRequestPath(HttpServletRequest request) {
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		return urlPathHelper.getPathWithinApplication(request);
    }

	private boolean isResourceHandler(Object handler) {
		return handler instanceof ResourceHttpRequestHandler;
	}
}
