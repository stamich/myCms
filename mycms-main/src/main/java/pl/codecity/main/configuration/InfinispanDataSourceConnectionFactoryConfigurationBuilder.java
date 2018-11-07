package pl.codecity.main.configuration;

import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.persistence.jdbc.configuration.AbstractJdbcStoreConfigurationBuilder;
import org.infinispan.persistence.jdbc.configuration.AbstractJdbcStoreConfigurationChildBuilder;
import org.infinispan.persistence.jdbc.configuration.ConnectionFactoryConfigurationBuilder;

import javax.sql.DataSource;

public class InfinispanDataSourceConnectionFactoryConfigurationBuilder<S extends AbstractJdbcStoreConfigurationBuilder<?, S>> extends AbstractJdbcStoreConfigurationChildBuilder<S>
		implements ConnectionFactoryConfigurationBuilder<InfinispanDataSourceConnectionFactoryConfiguration> {

	public InfinispanDataSourceConnectionFactoryConfigurationBuilder(AbstractJdbcStoreConfigurationBuilder<?, S> builder) {
		super(builder);
	}

	private DataSource dataSource;

	public void dataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void validate() {

	}

	@Override
	public void validate(GlobalConfiguration globalConfiguration) {

	}

	@Override
	public InfinispanDataSourceConnectionFactoryConfiguration create() {
		return new InfinispanDataSourceConnectionFactoryConfiguration(dataSource);
	}

	@Override
	public InfinispanDataSourceConnectionFactoryConfigurationBuilder<S> read(InfinispanDataSourceConnectionFactoryConfiguration template) {
		this.dataSource = template.dataSource();
		return this;
	}
}
