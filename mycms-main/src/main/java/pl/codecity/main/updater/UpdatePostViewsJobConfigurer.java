package pl.codecity.main.updater;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.inject.Inject;

@Import({UpdatePostViewsItemReader.class, UpdatePostViewsItemWriter.class})
public class UpdatePostViewsJobConfigurer {

	@Inject
	private JobBuilderFactory jobBuilders;

	@Inject
	private StepBuilderFactory stepBuilders;

	@Inject
	private UpdatePostViewsItemReader updatePostViewsItemReader;

	@Inject
	private UpdatePostViewsItemWriter updatePostViewsItemWriter;

	@Bean
	public Job updatePostViewsJob() {
		return jobBuilders.get("updatePostViewsJob")
				.start(updatePostViewsStep())
				.build();
	}

	public Step updatePostViewsStep() {
		return stepBuilders.get("updatePostViewsStep")
				.chunk(10)
				.reader((ItemReader) updatePostViewsItemReader)
				.writer((ItemWriter) updatePostViewsItemWriter)
				.build();
	}
}
