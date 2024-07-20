package store.novabook.coupon.common.util;

import static store.novabook.coupon.common.exception.ErrorCode.*;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import store.novabook.coupon.common.dto.DatabaseConfigDto;
import store.novabook.coupon.common.dto.RabbitMQConfigDto;
import store.novabook.coupon.common.dto.RedisConfigDto;
import store.novabook.coupon.common.exception.KeyManagerException;

/**
 * Key Manager 유틸리티 클래스입니다.
 *
 * <p>이 클래스는 NHN 클라우드 Key Manager API를 사용하여 보안 비밀을 가져오는 메서드를 제공합니다.</p>
 */
@Slf4j
public class KeyManagerUtil {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	private KeyManagerUtil() {
		// 유틸리티 클래스의 인스턴스화 방지
	}

	/**
	 * Key Manager API를 사용하여 데이터 소스를 가져옵니다.
	 *
	 * @param environment Spring 환경 객체
	 * @param keyid       보안 비밀 키 ID
	 * @return 데이터 소스 문자열
	 * @throws KeyManagerException 비밀 키를 가져오지 못한 경우 예외 발생
	 */
	private static String getDataSource(Environment environment, String keyid) {
		String appkey = environment.getProperty("nhn.cloud.keyManager.appkey");
		String userId = environment.getProperty("nhn.cloud.keyManager.userAccessKey");
		String secretKey = environment.getProperty("nhn.cloud.keyManager.secretAccessKey");

		RestTemplate restTemplate = new RestTemplate();
		String baseUrl = "https://api-keymanager.nhncloudservice.com/keymanager/v1.2/appkey/{appkey}/secrets/{keyid}";
		String url = baseUrl.replace("{appkey}", appkey).replace("{keyid}", keyid);
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-TC-AUTHENTICATION-ID", userId);
		headers.set("X-TC-AUTHENTICATION-SECRET", secretKey);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
			new ParameterizedTypeReference<Map<String, Object>>() {
			});

		var body = getStringObjectMap(response);

		String result = (String)body.get("secret");
		if (result.isEmpty()) {
			log.error("\"secret\" key is missing in response body");
			log.error("{}", body);
			throw new KeyManagerException(MISSING_BODY_KEY);
		}

		return result;
	}

	/**
	 * Key Manager API 응답에서 바디 부분을 가져옵니다.
	 *
	 * @param response Key Manager API 응답 객체
	 * @return 바디 부분을 담은 맵 객체
	 * @throws KeyManagerException 응답 바디가 없거나 잘못된 경우 예외 발생
	 */
	private static @NotNull Map<String, Object> getStringObjectMap(ResponseEntity<Map<String, Object>> response) {
		if (response.getBody() == null) {
			throw new KeyManagerException(RESPONSE_BODY_IS_NULL);
		}

		Object bodyObj = response.getBody().get("body");
		if (bodyObj == null) {
			throw new KeyManagerException(MISSING_BODY_KEY);
		}

		Map<String, Object> body;
		try {
			body = TypeUtil.castMap(bodyObj, String.class, Object.class);
		} catch (ClassCastException e) {
			throw new KeyManagerException(MISSING_BODY_KEY);
		}

		String result = (String)body.get("secret");
		if (result == null || result.isEmpty()) {
			log.error("\"secret\" key is missing or empty in response body");
			log.error("{}", body);
			throw new KeyManagerException(MISSING_SECRET_KEY);
		}

		return body;
	}

	/**
	 * Key Manager API를 사용하여 데이터베이스 구성 정보를 가져옵니다.
	 *
	 * @param environment Spring 환경 객체
	 * @return 데이터베이스 구성 정보 DTO
	 * @throws KeyManagerException 데이터베이스 구성 정보를 가져오지 못한 경우 예외 발생
	 */
	public static DatabaseConfigDto getDatabaseConfig(Environment environment) {
		try {
			String keyid = environment.getProperty("nhn.cloud.keyManager.couponKey");
			return objectMapper.readValue(getDataSource(environment, keyid), DatabaseConfigDto.class);
		} catch (JsonProcessingException e) {
			log.error("DatabaseConfig{}", FAILED_CONVERSION.getMessage());
			throw new KeyManagerException(FAILED_CONVERSION);
		}
	}

	/**
	 * Key Manager API를 사용하여 Redis 구성 정보를 가져옵니다.
	 *
	 * @param environment Spring 환경 객체
	 * @return Redis 구성 정보 DTO
	 * @throws KeyManagerException Redis 구성 정보를 가져오지 못한 경우 예외 발생
	 */
	public static RedisConfigDto getRedisConfig(Environment environment) {
		try {
			String keyid = environment.getProperty("nhn.cloud.keyManager.redisKey");
			return objectMapper.readValue(getDataSource(environment, keyid), RedisConfigDto.class);
		} catch (JsonProcessingException e) {
			log.error("RedisConfig{}", FAILED_CONVERSION.getMessage());
			throw new KeyManagerException(FAILED_CONVERSION);
		}
	}

	/**
	 * Key Manager API를 사용하여 RabbitMQ 구성 정보를 가져옵니다.
	 *
	 * @param environment Spring 환경 객체
	 * @return RabbitMQ 구성 정보 DTO
	 * @throws KeyManagerException RabbitMQ 구성 정보를 가져오지 못한 경우 예외 발생
	 */
	public static RabbitMQConfigDto getRabbitMQConfig(Environment environment) {
		try {
			String keyid = environment.getProperty("nhn.cloud.keyManager.rabbitMQKey");
			return objectMapper.readValue(getDataSource(environment, keyid), RabbitMQConfigDto.class);
		} catch (JsonProcessingException e) {
			log.error("RabbitMQConfig{}", FAILED_CONVERSION.getMessage());
			throw new KeyManagerException(FAILED_CONVERSION);
		}
	}
}
