package pl.codecity.main.updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import pl.codecity.main.controller.guest.ArticleDescribeController;
import pl.codecity.main.controller.guest.PageDescribeController;
import pl.codecity.main.controller.support.BlogLanguageRewriteMatch;
import pl.codecity.main.controller.support.BlogLanguageRewriteRule;
import pl.codecity.main.exception.ServiceException;
import pl.codecity.main.model.Post;
import pl.codecity.main.repository.PostRepository;
import pl.codecity.main.service.BlogService;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@Component
@StepScope
public class UpdatePostViewsItemWriter implements ItemWriter<List> {

	@Inject
	private ServletContext servletContext;

	@Autowired
	private BlogService blogService;

	@Autowired
	private PostRepository postRepository;

	private static Logger logger = LoggerFactory.getLogger(UpdatePostViewsItemWriter.class);

	@Override
	public void write(List<? extends List> items) throws Exception {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext, "org.springframework.web.servlet.FrameworkServlet.CONTEXT.guestServlet");
		if (context == null) {
			throw new ServiceException("GuestServlet is not ready yet");
		}

		final RequestMappingHandlerMapping mapping = context.getBean(RequestMappingHandlerMapping.class);

		for (List item : items) {
			UriComponents uriComponents = UriComponentsBuilder.fromUriString((String) item.get(0)).build();
			logger.info("Processing [{}]", uriComponents.toString());

			MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
			request.setMethod("GET");
			request.setRequestURI(uriComponents.getPath());
			request.setQueryString(uriComponents.getQuery());
			MockHttpServletResponse response = new MockHttpServletResponse();

			BlogLanguageRewriteRule rewriteRule = new BlogLanguageRewriteRule(blogService);
			BlogLanguageRewriteMatch rewriteMatch = (BlogLanguageRewriteMatch) rewriteRule.matches(request, response);
			try {
				rewriteMatch.execute(request, response);
			} catch (ServletException e) {
				throw new ServiceException(e);
			} catch (IOException e) {
				throw new ServiceException(e);
			}

			request.setRequestURI(rewriteMatch.getMatchingUrl());

			HandlerExecutionChain handler;
			try {
				handler = mapping.getHandler(request);
			} catch (Exception e) {
				throw new ServiceException(e);
			}

			if (!(handler.getHandler() instanceof HandlerMethod)) {
				continue;
			}

			HandlerMethod method = (HandlerMethod) handler.getHandler();
			if (!method.getBeanType().equals(ArticleDescribeController.class) && !method.getBeanType().equals(PageDescribeController.class)) {
				continue;
			}

			// Last path mean code of post
			String code = uriComponents.getPathSegments().get(uriComponents.getPathSegments().size() - 1);
			Post post = postRepository.findOneByCodeAndLanguage(code, rewriteMatch.getBlogLanguage().getLanguage());
			if (post == null) {
				logger.debug("Post not found [{}]", code);
				continue;
			}

			logger.info("Update the PageView. Post ID [{}]: {} -> {}", post.getId(), post.getViews(), item.get(1));
			post.setViews(Long.parseLong((String) item.get(1)));
			postRepository.saveAndFlush(post);
		}
	}
}
