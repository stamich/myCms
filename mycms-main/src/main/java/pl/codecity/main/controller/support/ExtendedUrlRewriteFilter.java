package pl.codecity.main.controller.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.tuckey.web.filters.urlrewrite.*;
import org.tuckey.web.filters.urlrewrite.extend.RewriteMatch;
import org.tuckey.web.filters.urlrewrite.extend.RewriteRule;
import pl.codecity.main.service.BlogService;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ExtendedUrlRewriteFilter extends UrlRewriteFilter {

	@Autowired
	private BlogService blogService;

	private static Logger logger = LoggerFactory.getLogger(UrlRewriteFilter.class);

	@Override
	protected void loadUrlRewriter(FilterConfig filterConfig) throws ServletException {
		try {
			Conf conf = new Conf();
			conf.addRule(new RuleImpl(new BlogLanguageRewriteRule(blogService)));
			conf.initialise();
			checkConf(conf);
		} catch(Throwable e) {
			logger.error("Can not read ", e);
			throw new ServletException(e);
		}
	}

	private class RuleImpl implements Rule {

		private int id;

		private RewriteRule rewriteRule;

		private RuleImpl(RewriteRule rewriteRule) {
			this.rewriteRule = rewriteRule;
		}

		@Override
		public RewrittenUrl matches(String url, HttpServletRequest request, HttpServletResponse response, RuleChain ruleChain) throws IOException, ServletException, InvocationTargetException {
			return this.matches(url, request, response);
		}

		@Override
		public RewrittenUrl matches(String url, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InvocationTargetException {
			RewriteMatch match = rewriteRule.matches(request, response);
			return match != null ? new RewrittenUrlImpl(match) : null;
		}

		@Override
		public boolean initialise(ServletContext servletContext) {
			return true;
		}

		@Override
		public void destroy() {
			rewriteRule.destroy();
		}

		@Override
		public String getName() {
			return this.rewriteRule.getClass().getName();
		}

		@Override
		public String getDisplayName() {
			return "Class Rule " + this.rewriteRule.getClass().getName();
		}

		@Override
		public boolean isLast() {
			return true;
		}

		@Override
		public void setId(int id) {
			this.id = id;
		}

		@Override
		public int getId() {
			return this.id;
		}

		@Override
		public boolean isValid() {
			return true;
		}

		@Override
		public boolean isFilter() {
			return false;
		}

		@Override
		public List getErrors() {
			return null;
		}
	}

	private class RewrittenUrlImpl implements RewrittenUrl {

		private RewriteMatch rewriteMatch;

		private String matchingUrl;

		protected RewrittenUrlImpl(RewriteMatch rewriteMatch) {
			this.matchingUrl = rewriteMatch.getMatchingUrl();
			this.rewriteMatch = rewriteMatch;
		}

		public boolean doRewrite(HttpServletRequest hsRequest, HttpServletResponse hsResponse, FilterChain chain) throws IOException, ServletException {
			return this.rewriteMatch.execute(hsRequest, hsResponse);
		}

		public String getTarget() {
			return this.matchingUrl;
		}
	}
}
