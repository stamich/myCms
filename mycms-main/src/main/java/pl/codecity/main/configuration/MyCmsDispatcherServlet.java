package pl.codecity.main.configuration;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyCmsDispatcherServlet extends DispatcherServlet {

	private boolean detectParentHandlerMappings;

	private List<HandlerMapping> parentHandlerMappings;

	public MyCmsDispatcherServlet(WebApplicationContext webApplicationContext) {
		super(webApplicationContext);
	}

	public void setDetectParentHandlerMappings(boolean detectParentHandlerMappings) {
		this.detectParentHandlerMappings = detectParentHandlerMappings;
	}

	@Override
	protected void initStrategies(ApplicationContext context) {
		super.initStrategies(context);

		if (this.detectParentHandlerMappings) {
			this.parentHandlerMappings = null;
			if (getWebApplicationContext().getParent() != null) {
				Map<String, HandlerMapping> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(getWebApplicationContext().getParent(), HandlerMapping.class, true, false);
				if (!matchingBeans.isEmpty()) {
					this.parentHandlerMappings = new ArrayList<>(matchingBeans.values());
					AnnotationAwareOrderComparator.sort(this.parentHandlerMappings);
				}
			}
		}
	}

	@Override
	protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		if (!CollectionUtils.isEmpty(this.parentHandlerMappings)) {
			for (HandlerMapping hm : this.parentHandlerMappings) {
				if (logger.isTraceEnabled()) {
					logger.trace("Testing handler map [" + hm + "] in DispatcherServlet with name '" + getServletName() + "'");
				}
				HandlerExecutionChain handler = hm.getHandler(request);
				if (handler != null) {
					return handler;
				}
			}
		}
		return super.getHandler(request);
	}
}
