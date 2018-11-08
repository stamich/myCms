package pl.codecity.main.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.boot.web.servlet.filter.OrderedHttpPutFormContentFilter;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;
import pl.codecity.main.controller.support.ExtendedUrlRewriteFilter;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

@Configuration
public class MyCmsServletConfiguration implements ResourceLoaderAware {

	public static final String GUEST_SERVLET_NAME = "guestServlet";
	public static final String GUEST_SERVLET_PATH = "";

	public static final String ADMIN_SERVLET_NAME = "adminServlet";
	public static final String ADMIN_SERVLET_PATH = "/_admin";

	private ResourceLoader resourceLoader;

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Bean
	@ConditionalOnMissingBean(HiddenHttpMethodFilter.class)
	public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new OrderedHiddenHttpMethodFilter();
	}

	@Bean
	@ConditionalOnMissingBean(HttpPutFormContentFilter.class)
	public OrderedHttpPutFormContentFilter httpPutFormContentFilter() {
		return new OrderedHttpPutFormContentFilter();
	}

	@Bean
	@ConditionalOnMissingBean(UrlRewriteFilter.class)
	public UrlRewriteFilter urlRewriteFilter() {
		return new ExtendedUrlRewriteFilter();
	}

	@Bean
	public FilterRegistrationBean urlRewriteFilterRegistration() {
		DelegatingFilterProxy proxy = new DelegatingFilterProxy("urlRewriteFilter");
		proxy.setTargetFilterLifecycle(true);

		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setName("urlRewriteFilter");
		registration.setFilter(proxy);
		registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST));
		registration.addUrlPatterns("/*");
		registration.setOrder(0);
		return registration;
	}

	@Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
	public DispatcherServlet guestDispatcherServlet() {
		AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext();
		context.setResourceLoader(getResourceLoader());
		context.register(WebGuestConfiguration.class);
		MyCmsDispatcherServlet dispatcherServlet = new MyCmsDispatcherServlet(context);
		dispatcherServlet.setDetectParentHandlerMappings(true);
		return dispatcherServlet;
	}

	@Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME)
	public ServletRegistrationBean guestServletRegistrationBean(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
		registration.setName(GUEST_SERVLET_NAME);
		registration.setLoadOnStartup(1);
		registration.addUrlMappings(GUEST_SERVLET_PATH + "/*");
		return registration;
	}

	@Bean
	public DispatcherServlet adminDispatcherServlet() {
		AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext();
		context.setResourceLoader(getResourceLoader());
		context.register(WebAdminConfiguration.class);
		MyCmsDispatcherServlet dispatcherServlet = new MyCmsDispatcherServlet(context);
		return dispatcherServlet;
	}

	@Bean
	public ServletRegistrationBean adminServletRegistrationBean() {
		ServletRegistrationBean registration = new ServletRegistrationBean(adminDispatcherServlet());
		registration.setName(ADMIN_SERVLET_NAME);
		registration.setLoadOnStartup(2);
		registration.addUrlMappings(ADMIN_SERVLET_PATH + "/*");
		return registration;
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}
}
