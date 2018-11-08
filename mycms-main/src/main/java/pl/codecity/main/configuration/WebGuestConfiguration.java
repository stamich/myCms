package pl.codecity.main.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import pl.codecity.main.controller.guest.*;
import pl.codecity.main.service.BlogService;
import pl.codecity.main.service.PageService;

@Configuration
public class WebGuestConfiguration extends DelegatingWebMvcConfiguration {

	@Autowired
	private PageDescribeController pageDescribeController;

	@Override
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping handlerMapping = super.requestMappingHandlerMapping();
		handlerMapping.setOrder(Ordered.LOWEST_PRECEDENCE);
		return handlerMapping;
	}

	@Override
	protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
		RequestMappingHandlerMapping handlerMapping = super.createRequestMappingHandlerMapping();
		handlerMapping.setDefaultHandler(pageDescribeController);
		return handlerMapping;
	}

	@Override
	protected RequestMappingHandlerAdapter createRequestMappingHandlerAdapter() {
		return super.createRequestMappingHandlerAdapter();
	}

	@Configuration
	public static class ControllerConfigration {

		@Autowired
		private BlogService blogService;

		@Autowired
		private PageService pageService;

		@Bean
		@ConditionalOnMissingBean
		public PageDescribeController pageDescribeController() {
			return new PageDescribeController(blogService, pageService);
		}

		@Bean
		@ConditionalOnMissingBean
		public ArticleDescribeController articleDescribeController() {
			return new ArticleDescribeController();
		}

		@Bean
		@ConditionalOnMissingBean
		public ArticleIndexController articleIndexController() {
			return new ArticleIndexController();
		}

		@Bean
		@ConditionalOnMissingBean
		public CommentRestController commentRestController() {
			return new CommentRestController();
		}

		@Bean
		@ConditionalOnMissingBean
		public LoginController loginController() {
			return new LoginController();
		}

		@Bean
		@ConditionalOnMissingBean
		public PasswordResetController passwordResetController() {
			return new PasswordResetController();
		}

		@Bean
		@ConditionalOnMissingBean
		public PasswordUpdateController passwordUpdateController() {
			return new PasswordUpdateController();
		}

		@Bean
		@ConditionalOnMissingBean
		public ProfileUpdateController profileUpdateController() {
			return new ProfileUpdateController();
		}

		@Bean
		@ConditionalOnMissingBean
		public SignupController signupController() {
			return new SignupController();
		}

		@Bean
		@ConditionalOnMissingBean
		public FeedController feedController() {
			return new FeedController();
		}

		@Bean
		@ConditionalOnMissingBean
		public IndexController indexController() {
			return new IndexController();
		}

		@Bean
		@ConditionalOnMissingBean
		public SearchController searchController() {
			return new SearchController();
		}

		@Bean
		@ConditionalOnMissingBean
		public CategoryController categoryController() {
			return new CategoryController();
		}
		
		@Bean
		@ConditionalOnMissingBean
		public TagController tagController() {
			return new TagController();
		}
	}
}
