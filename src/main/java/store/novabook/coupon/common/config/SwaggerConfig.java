package store.novabook.coupon.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Swagger 설정 클래스.
 * Swagger UI를 통해 API 명세서를 자동 생성합니다.
 */
@Configuration
public class SwaggerConfig {

	/**
	 * OpenAPI 객체를 생성합니다.
	 *
	 * @return OpenAPI 객체
	 */
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().components(new Components()).info(apiInfo());
	}

	/**
	 * API 정보 객체를 생성합니다.
	 *
	 * @return API 정보 객체
	 */
	private Info apiInfo() {
		return new Info().title("NovaBook Coupon API")
			.description("Springdoc-Swagger NOVABOOK Coupon 명세서")
			.version("0.0.1");
	}
}
