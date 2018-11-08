package pl.codecity.main.controller.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UrlPathHelper;
import org.tuckey.web.filters.urlrewrite.extend.RewriteMatch;
import pl.codecity.main.model.BlogLanguage;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BlogLanguageRewriteMatch extends RewriteMatch {

	private BlogLanguage blogLanguage;
	private String matchingUrl;

	private static Logger logger = LoggerFactory.getLogger(BlogLanguageRewriteMatch.class);

	public BlogLanguageRewriteMatch(BlogLanguage blogLanguage) {
		this.blogLanguage = blogLanguage;
	}

	public BlogLanguage getBlogLanguage() {
		return blogLanguage;
	}

	@Override
	public String getMatchingUrl() {
		return matchingUrl;
	}

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		String originalPath = urlPathHelper.getLookupPathForRequest(request);

		String rewritePath = originalPath.replaceAll("^/" + blogLanguage.getLanguage() + "/", "/");
		matchingUrl = rewritePath;
		logger.debug(originalPath + " => " + rewritePath);

		request.setAttribute(BlogLanguageMethodArgumentResolver.BLOG_LANGUAGE_ATTRIBUTE, blogLanguage);

		RequestDispatcher rd = request.getRequestDispatcher(urlPathHelper.getServletPath(request) + rewritePath);
		rd.forward(request, response);
		return true;
	}
}
