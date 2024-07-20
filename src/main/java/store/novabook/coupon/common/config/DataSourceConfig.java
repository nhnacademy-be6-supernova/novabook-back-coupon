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

/**
 * 데이터 소스 설정을 담당하는 구성 클래스입니다.
 * 프로덕션 및 개발 환경에서 사용될 데이터 소스를 설정합니다.
 */
@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

	private final Environment environment;

	/**
	 * 프로덕션 및 개발 환경에서 사용할 데이터 소스를 생성합니다.
	 *
	 * @return 설정된 데이터 소스
	 */
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
		dataSource.setInitialSize(
			Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.datasource.dbcp2.initial-size"))));
		dataSource.setMaxIdle(
			Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.datasource.dbcp2.max-idle"))));
		dataSource.setMinIdle(
			Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.datasource.dbcp2.min-idle"))));
		dataSource.setValidationQuery(environment.getProperty("spring.datasource.dbcp2.validation-query"));
		dataSource.setDefaultAutoCommit(
			Boolean.parseBoolean(environment.getProperty("spring.datasource.dbcp2.default-auto-commit")));
		return dataSource;
	}
}
