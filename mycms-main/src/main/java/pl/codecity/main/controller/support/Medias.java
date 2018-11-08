package pl.codecity.main.controller.support;

import org.thymeleaf.context.IExpressionContext;
import pl.codecity.main.configuration.MyCmsProperties;
import pl.codecity.main.model.Media;

public class Medias {

	private IExpressionContext context;

	private MyCmsProperties myCmsProperties;

	public Medias(IExpressionContext context, MyCmsProperties myCmsProperties) {
		this.context = context;
		this.myCmsProperties = myCmsProperties;
	}

	public String link(Media media) {
		return link(media.getId());
	}

	public String link(String id) {
		return myCmsProperties.getMediaUrlPrefix() + id;
	}
}
