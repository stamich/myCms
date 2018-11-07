package pl.codecity.main.updater;

import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.model.GaData;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import pl.codecity.main.exception.GoogleAnalyticsException;
import pl.codecity.main.model.Blog;
import pl.codecity.main.model.GoogleAnalytics;
import pl.codecity.main.service.BlogService;
import pl.codecity.main.utility.GoogleAnalyticsUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@StepScope
public class UpdatePostViewsItemReader extends AbstractPagingItemReader<List> {

	@Inject
	private BlogService blogService;

	public UpdatePostViewsItemReader() {
		setPageSize(GoogleAnalyticsUtils.MAX_RESULTS);
	}

	@Override
	protected void doReadPage() {
		if (results == null) {
			results = new CopyOnWriteArrayList<>();
		}
		else {
			results.clear();
		}

		Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
		if (blog == null) {
			logger.warn("Configuration of Default Blog can not be found");
			return;
		}
		GoogleAnalytics googleAnalytics = blog.getGoogleAnalytics();
		if (googleAnalytics == null) {
			logger.warn("Configuration of Google Analytics can not be found");
			return;
		}

		Analytics analytics = GoogleAnalyticsUtils.buildClient(googleAnalytics);

		try {
			LocalDate now = LocalDate.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			Analytics.Data.Ga.Get request = analytics.data().ga()
					.get(googleAnalytics.getProfileId(), now.minusYears(1).format(dateTimeFormatter), now.format(dateTimeFormatter), "ga:pageViews")
//						.setDimensions(String.format("ga:dimension%d", googleAnalytics.getCustomDimensionIndex()))
//						.setSort(String.format("-ga:dimension%d", googleAnalytics.getCustomDimensionIndex()))
					.setDimensions(String.format("ga:pagePath", googleAnalytics.getCustomDimensionIndex()))
					.setSort(String.format("-ga:pageViews", googleAnalytics.getCustomDimensionIndex()))
					.setStartIndex(getPage() * getPageSize() + 1)
					.setMaxResults(getPageSize());

			logger.info(request.toString());
			final GaData gaData = request.execute();
			if (CollectionUtils.isEmpty(gaData.getRows())) {
				return;
			}

			results.addAll(gaData.getRows());
		} catch (IOException e) {
			logger.warn("Failed to synchronize with Google Analytics", e);
			throw new GoogleAnalyticsException(e);
		}

//		logger.info("Synchronization to google analytics is now COMPLETE. {} posts updated.", count);
	}

	@Override
	protected void doJumpToPage(int i) {
	}
}
