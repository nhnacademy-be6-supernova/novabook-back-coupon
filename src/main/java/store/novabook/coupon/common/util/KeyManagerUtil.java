package store.novabook.coupon.common.util;

import static store.novabook.coupon.common.exception.ErrorCode.*;

import java.util.Map;
import java.util.Objects;

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

@Slf4j
public class KeyManagerUtil {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	private KeyManagerUtil() {
	}

	static String getDataSource(Environment environment, String keyid, RestTemplate restTemplate) {

		String appkey = environment.getProperty("nhn.cloud.keyManager.appkey");
		String userId = environment.getProperty("nhn.cloud.keyManager.userAccessKey");
		String secretKey = environment.getProperty("nhn.cloud.keyManager.secretAccessKey");

		String baseUrl = "https://api-keymanager.nhncloudservice.com/keymanager/v1.2/appkey/{appkey}/secrets/{keyid}";
		String url = baseUrl.replace("{appkey}", Objects.requireNonNull(appkey)).replace("{keyid}", keyid);
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-TC-AUTHENTICATION-ID", userId);
		headers.set("X-TC-AUTHENTICATION-SECRET", secretKey);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
			new ParameterizedTypeReference<>() {
			});

		return getStringObjectMap(response);
	}

	private static String getStringObjectMap(ResponseEntity<Map<String, Object>> response) {
		if (response.getBody() == null) {
			throw new KeyManagerException(RESPONSE_BODY_IS_NULL);
		}
		Object bodyObj = response.getBody().get("body");

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

		return result;
	}

	public static DatabaseConfigDto getDatabaseConfig(Environment environment, RestTemplate restTemplate) {
		// JSON 문자열을 DTO로 변환
		try {
			String keyid = environment.getProperty("nhn.cloud.keyManager.couponKey");
			return objectMapper.readValue(getDataSource(environment, keyid, restTemplate), DatabaseConfigDto.class);
		} catch (JsonProcessingException e) {
			log.error("DatabaseConfig{}", FAILED_CONVERSION.getMessage());
			throw new KeyManagerException(FAILED_CONVERSION);
		}
	}

	public static RedisConfigDto getRedisConfig(Environment environment, RestTemplate restTemplate) {
		try {
			String keyid = environment.getProperty("nhn.cloud.keyManager.redisKey");
			return objectMapper.readValue(getDataSource(environment, keyid, restTemplate), RedisConfigDto.class);
		} catch (JsonProcessingException e) {
			//오류처리
			log.error("RedisConfig{}", FAILED_CONVERSION.getMessage());
			throw new KeyManagerException(FAILED_CONVERSION);
		}
	}

	public static RabbitMQConfigDto getRabbitMQConfig(Environment environment, RestTemplate restTemplate) {
		try {
			String keyid = environment.getProperty("nhn.cloud.keyManager.rabbitMQKey");
			return objectMapper.readValue(getDataSource(environment, keyid, restTemplate), RabbitMQConfigDto.class);
		} catch (JsonProcessingException e) {
			//오류처리
			log.error("RabbitMQConfig{}", FAILED_CONVERSION.getMessage());
			throw new KeyManagerException(FAILED_CONVERSION);
		}
	}

}
