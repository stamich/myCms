package pl.codecity.main.utility;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.SecurityUtils;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.codecity.main.exception.GoogleAnalyticsException;
import pl.codecity.main.model.GoogleAnalytics;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.HashSet;
import java.util.Set;

public abstract class GoogleAnalyticsUtils {

	public static final int MAX_RESULTS = 10000;
	public static final int MAX_RETRY = 5;
	public static final int RETRY_INTERVAL = 5000; // milliseconds

	private static Logger logger = LoggerFactory.getLogger(GoogleAnalyticsUtils.class);

	public static Analytics buildClient(GoogleAnalytics googleAnalytics) {
		Analytics analytics;
		try {
			PrivateKey privateKey= SecurityUtils.loadPrivateKeyFromKeyStore(
					SecurityUtils.getPkcs12KeyStore(), new ByteArrayInputStream(googleAnalytics.getServiceAccountP12FileContent()),
					"notasecret", "privatekey", "notasecret");

			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

			Set<String> scopes = new HashSet<>();
			scopes.add(AnalyticsScopes.ANALYTICS_READONLY);

			final GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
					.setJsonFactory(jsonFactory)
					.setServiceAccountId(googleAnalytics.getServiceAccountId())
					.setServiceAccountScopes(scopes)
					.setServiceAccountPrivateKey(privateKey)
					.build();

			HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {
				@Override
				public void initialize(HttpRequest httpRequest) throws IOException {
					credential.initialize(httpRequest);
					httpRequest.setConnectTimeout(3 * 60000);  // 3 minutes connect timeout
					httpRequest.setReadTimeout(3 * 60000);  // 3 minutes read timeout
				}
			};
			analytics = new Analytics.Builder(httpTransport, jsonFactory, httpRequestInitializer)
					.setApplicationName("WallRide")
					.build();
		} catch (Exception e) {
			logger.warn("Failed to synchronize with Google Analytics", e);
			throw new GoogleAnalyticsException(e);
		}
		return analytics;
	}
}
