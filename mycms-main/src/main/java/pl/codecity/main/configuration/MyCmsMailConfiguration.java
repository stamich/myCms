package pl.codecity.main.configuration;

import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class MyCmsMailConfiguration extends MailSenderAutoConfiguration {

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private MyCmsThymeleafDialect myCmsThymeleafDialect;

	@Inject
	private Environment environment;

	@Inject
	private ThymeleafProperties properties;

//	public WallRideMailConfiguration(MailProperties properties, ObjectProvider<Session> sessionProvider) {
//		super(properties, sessionProvider);
//	}

	@Bean(name = "emailTemplateResolver")
	public ITemplateResolver emailTemplateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//		resolver.setResourceResolver(wallRideResourceResourceResolver);
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix(environment.getRequiredProperty("spring.thymeleaf.prefix.mail"));
		resolver.setSuffix(this.properties.getSuffix());
		resolver.setTemplateMode(this.properties.getMode());
		resolver.setCharacterEncoding(this.properties.getEncoding().name());
		resolver.setCacheable(this.properties.isCache());
		resolver.setOrder(1);
		return resolver;
	}

	@Bean(name = "emailTemplateEngine")
	public SpringTemplateEngine emailTemplateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		Set<ITemplateResolver> resolvers = new HashSet<>();
		resolvers.add(emailTemplateResolver());
		engine.setTemplateResolvers(resolvers);

		Set<IDialect> dialects = new HashSet<>();
		dialects.add(myCmsThymeleafDialect);
		dialects.add(new Java8TimeDialect());
		engine.setAdditionalDialects(dialects);
		return engine;
	}
}
