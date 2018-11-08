package pl.codecity.main.controller.admin.media;

import pl.codecity.main.configuration.MyCmsProperties;
import pl.codecity.main.model.Media;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

public class MediaIndexModel implements Serializable {

	private String url;

	private String thumb;

	private String name;

	private String tag;

	public MediaIndexModel(Media media, MyCmsProperties myCmsProperties) {
		this.url = myCmsProperties.getMediaUrlPrefix() + media.getId();
		this.thumb = myCmsProperties.getMediaUrlPrefix() + media.getId() + "?w=100&h=100&m=1";
		this.name = media.getOriginalName();
		this.tag = media.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM"));
	}

	public String getUrl() {
		return url;
	}

	public String getThumb() {
		return thumb;
	}

	public String getName() {
		return name;
	}

	public String getTag() {
		return tag;
	}
}
