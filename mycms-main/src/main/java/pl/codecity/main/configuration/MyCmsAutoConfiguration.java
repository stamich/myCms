package pl.codecity.main.configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import pl.codecity.main.repository.BlogRepository;
import pl.codecity.main.service.BlogService;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(MyCmsProperties.class)
@EnableAsync
@EnableJpaRepositories(basePackageClasses = BlogRepository.class)
@Import({MyCmsCacheConfiguration.class, MyCmsJobConfiguration.class, MyCmsJpaConfiguration.class, MyCmsMailConfiguration.class, MyCmsMessageSourceConfiguration.class,
		MyCmsScheduleConfiguration.class, MyCmsSecurityConfiguration.class, MyCmsServletConfiguration.class,
		MyCmsThymeleafConfiguration.class, MyCmsWebMvcConfiguration.class,})
@ComponentScan(basePackageClasses = BlogService.class)
public class MyCmsAutoConfiguration {

//	@Bean
//	public WallRideResourceResourceResolver wallRideResourceResourceResolver() {
//		return new WallRideResourceResourceResolver();
//	}

	@Bean
	public AmazonS3 amazonS3() {
//		final String accessKey = environment.getRequiredProperty("aws.accessKey");
//		final String secretKey = environment.getRequiredProperty("aws.secretKey");
		ClientConfiguration configuration = new ClientConfiguration();
		configuration.setMaxConnections(1000);
//		return new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey), configuration);
		return new AmazonS3Client(configuration);
	}
}
