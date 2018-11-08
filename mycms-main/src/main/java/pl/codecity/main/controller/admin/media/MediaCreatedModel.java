package pl.codecity.main.controller.admin.media;

import pl.codecity.main.configuration.MyCmsProperties;
import pl.codecity.main.model.Media;

import java.io.Serializable;

public class MediaCreatedModel implements Serializable {

	private String id;

	private String link;

	private String name;

	public MediaCreatedModel(Media media, MyCmsProperties myCmsProperties) {
		this.id = media.getId();
		this.link = myCmsProperties.getMediaUrlPrefix() + media.getId();
		this.name = media.getOriginalName();
	}

	public String getId() {
		return id;
	}

	public String getLink() {
		return link;
	}

	public String getName() {
		return name;
	}
}
