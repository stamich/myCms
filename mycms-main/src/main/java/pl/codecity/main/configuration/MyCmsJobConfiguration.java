package pl.codecity.main.configuration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.codecity.main.updater.UpdatePostViewsJobConfigurer;

@Configuration
@Import({UpdatePostViewsJobConfigurer.class,})
@EnableBatchProcessing
public class MyCmsJobConfiguration {
}
