package pl.codecity.main.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;

public class MyCmsResourceTemplateResolver extends AbstractConfigurableTemplateResolver implements ApplicationContextAware {

	private ApplicationContext applicationContext = null;

	public MyCmsResourceTemplateResolver() {
		setCheckExistence(true);
	}

	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, String resourceName, String characterEncoding, Map<String, Object> templateResolutionAttributes) {
		return new MyCmsResourceTemplateResource(this.applicationContext, resourceName, characterEncoding);
	}
}
