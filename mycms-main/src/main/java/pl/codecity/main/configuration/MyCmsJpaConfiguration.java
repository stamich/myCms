package pl.codecity.main.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import pl.codecity.main.model.DomainObject;

@Configuration
@EntityScan(basePackageClasses = DomainObject.class)
public class MyCmsJpaConfiguration /*extends HibernateJpaAutoConfiguration*/ {

    /*
	public WallRideJpaConfiguration(DataSource dataSource, JpaProperties jpaProperties, ObjectProvider<JtaTransactionManager> jtaTransactionManager, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
		super(dataSource, jpaProperties, jtaTransactionManager, transactionManagerCustomizers);
	}

	@Bean
	@DependsOn("cacheManager")
	@Override
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factoryBuilder) {
		return super.entityManagerFactory(factoryBuilder);
	}
	*/
}
