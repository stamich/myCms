package pl.codecity.main.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import pl.codecity.main.controller.admin.DashboardController;
import pl.codecity.main.controller.support.*;
import pl.codecity.main.service.BlogService;
import pl.codecity.main.utility.CodeFormatAnnotationFormatterFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@ComponentScan(basePackageClasses = DashboardController.class)
public class WebAdminConfiguration extends DelegatingWebMvcConfiguration {

	@Autowired
	private MessageCodesResolver messageCodesResolver;

	@Autowired
	private MyCmsThymeleafDialect myCmsThymeleafDialect;

	@Autowired
	private BlogService blogService;

	@Autowired
	private DefaultModelAttributeInterceptor defaultModelAttributeInterceptor;

	@Autowired
	private SetupRedirectInterceptor setupRedirectInterceptor;

	@Autowired
	private Environment environment;

	@Autowired
	private ThymeleafProperties properties;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/admin/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.setOrder(Integer.MIN_VALUE);
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		super.addFormatters(registry);
		registry.addFormatterForFieldAnnotation(new CodeFormatAnnotationFormatterFactory());
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
		argumentResolvers.add(new AuthorizedUserMethodArgumentResolver());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
		webContentInterceptor.setCacheSeconds(0);
		webContentInterceptor.setUseExpiresHeader(true);
		webContentInterceptor.setUseCacheControlHeader(true);
		webContentInterceptor.setUseCacheControlNoStore(true);
		registry.addInterceptor(webContentInterceptor);

		registry.addInterceptor(defaultModelAttributeInterceptor);
		registry.addInterceptor(setupRedirectInterceptor);
	}

	@Override
	public MessageCodesResolver getMessageCodesResolver() {
		return messageCodesResolver;
	}

	// additional webmvc-related beans

	@Bean(name = "adminTemplateResolver")
	public ITemplateResolver adminTemplateResolver() {
		MyCmsResourceTemplateResolver resolver = new MyCmsResourceTemplateResolver();
//		resolver.setResourceResolver(MyCmsResourceResourceResolver);
		resolver.setApplicationContext(getApplicationContext());
		resolver.setPrefix(environment.getRequiredProperty("spring.thymeleaf.prefix.admin"));
		resolver.setSuffix(properties.getSuffix());
		resolver.setTemplateMode(properties.getMode());
		resolver.setCharacterEncoding(properties.getEncoding().name());
		resolver.setCacheable(properties.isCache());
		resolver.setOrder(2);
		return resolver;
	}

	@Bean(name = "guestTemplateResolver")
	public ITemplateResolver guestTemplateResolver() {
		MyCmsResourceTemplateResolver resolver = new MyCmsResourceTemplateResolver();
//		resolver.setResourceResolver(MyCmsResourceResourceResolver);
		resolver.setApplicationContext(getApplicationContext());
		resolver.setPrefix(environment.getRequiredProperty("spring.thymeleaf.prefix.guest"));
		resolver.setSuffix(this.properties.getSuffix());
		resolver.setTemplateMode(this.properties.getMode());
		resolver.setCharacterEncoding(this.properties.getEncoding().name());
		resolver.setCacheable(this.properties.isCache());
		resolver.setOrder(2);
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		Set<ITemplateResolver> resolvers = new HashSet<>();
		resolvers.add(adminTemplateResolver());
		engine.setTemplateResolvers(resolvers);

		Set<IDialect> dialects = new HashSet<>();
		dialects.add(new SpringSecurityDialect());
		dialects.add(new Java8TimeDialect());
		dialects.add(myCmsThymeleafDialect);
		engine.setAdditionalDialects(dialects);
		return engine;
	}

	@Bean
	public ThymeleafViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver viewResolver = new ExtendedThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setViewNames(this.properties.getViewNames());
		viewResolver.setCharacterEncoding(this.properties.getEncoding().name());
		viewResolver.setContentType(this.properties.getServlet().getContentType() + ";charset=" + this.properties.getEncoding());
		viewResolver.setCache(false);
		viewResolver.setOrder(1);
		return viewResolver;
	}

	@Bean
	public MultipartResolver multipartResolver() {
		return new CommonsMultipartResolver();
	}

	@Bean
	public LocaleResolver localeResolver() {
		PathVariableLocaleResolver pathVariableLocaleResolver = new PathVariableLocaleResolver();
		pathVariableLocaleResolver.setBlogService(blogService);
		return pathVariableLocaleResolver;
	}
}
