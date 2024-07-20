package store.novabook.coupon.common.config;

import java.util.Objects;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.dto.DatabaseConfigDto;
import store.novabook.coupon.common.util.KeyManagerUtil;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

	private final Environment environment;

	@Bean
	@Profile({"prod", "dev"})
	public DataSource storeDataSource() {
		RestTemplate restTemplate = new RestTemplate();
		DatabaseConfigDto config = KeyManagerUtil.getDatabaseConfig(environment, restTemplate);

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(environment.getProperty("spring.datasource.store.driver-class-name"));
		dataSource.setUrl(config.url());
		dataSource.setUsername(config.username());
		dataSource.setPassword(config.password());
		dataSource.setInitialSize(Integer.parseInt(
			Objects.requireNonNull(environment.getProperty("spring.datasource.dbcp2.initial-size"))));
		dataSource.setMaxIdle(Integer.parseInt(
			Objects.requireNonNull(environment.getProperty("spring.datasource.dbcp2.max-idle"))));
		dataSource.setMinIdle(Integer.parseInt(
			Objects.requireNonNull(environment.getProperty("spring.datasource.dbcp2.min-idle"))));
		dataSource.setValidationQuery(environment.getProperty("spring.datasource.dbcp2.validation-query"));
		dataSource.setDefaultAutoCommit(
			Boolean.parseBoolean(environment.getProperty("spring.datasource.dbcp2.default-auto-commit")));
		return dataSource;
	}

}
