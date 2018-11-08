package pl.codecity.main.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mycms")
public class MyCmsProperties {

	public static final String HOME_PROPERTY = "mycms.home";
	public static final String CONFIG_LOCATION_PROPERTY = "mycms.config-location";
	public static final String MEDIA_LOCATION_PROPERTY = "mycms.media-location";

	public static final String DEFAULT_CONFIG_PATH_NAME = "config/";
	public static final String DEFAULT_MEDIA_PATH_NAME = "media/";

	private String home;
	private String configLocation;
	private String mediaLocation;
	private String mediaUrlPrefix = "/media/";

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public String getMediaLocation() {
		return mediaLocation;
	}

	public void setMediaLocation(String mediaLocation) {
		this.mediaLocation = mediaLocation;
	}

	public String getMediaUrlPrefix() {
		return mediaUrlPrefix;
	}

	public void setMediaUrlPrefix(String mediaUrlPrefix) {
		this.mediaUrlPrefix = mediaUrlPrefix;
	}
}
