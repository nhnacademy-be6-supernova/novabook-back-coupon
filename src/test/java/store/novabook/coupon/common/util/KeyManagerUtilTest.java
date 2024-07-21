package store.novabook.coupon.common.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.coupon.common.dto.DatabaseConfigDto;
import store.novabook.coupon.common.dto.RabbitMQConfigDto;
import store.novabook.coupon.common.dto.RedisConfigDto;
import store.novabook.coupon.common.exception.KeyManagerException;

@ExtendWith(MockitoExtension.class)
class KeyManagerUtilTest {

	@Mock
	private Environment environment;

	@Mock
	private RestTemplate restTemplate;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private static final String APPKEY = "testAppkey";
	private static final String USER_ACCESS_KEY = "testUserAccessKey";
	private static final String SECRET_ACCESS_KEY = "testSecretAccessKey";

	@BeforeEach
	void setUp() {
		given(environment.getProperty("nhn.cloud.keyManager.appkey")).willReturn(APPKEY);
		given(environment.getProperty("nhn.cloud.keyManager.userAccessKey")).willReturn(USER_ACCESS_KEY);
		given(environment.getProperty("nhn.cloud.keyManager.secretAccessKey")).willReturn(SECRET_ACCESS_KEY);
	}

	@Test
	void getDatabaseConfig_returnsDatabaseConfig() {
		// given
		String keyid = "testDatabaseKey";
		given(environment.getProperty("nhn.cloud.keyManager.couponKey")).willReturn(keyid);

		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", Map.of("secret", "{\"url\":\"jdbc:mysql://localhost:3306/test\",\"username\":\"user\",\"password\":\"password\"}"));
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
			.willReturn(responseEntity);

		// when
		DatabaseConfigDto databaseConfig = KeyManagerUtil.getDatabaseConfig(environment, restTemplate);

		// then
		assertNotNull(databaseConfig);
		assertEquals("jdbc:mysql://localhost:3306/test", databaseConfig.url());
		assertEquals("user", databaseConfig.username());
		assertEquals("password", databaseConfig.password());
	}

	@Test
	void getRedisConfig_returnsRedisConfig() {
		// given
		String keyid = "testRedisKey";
		given(environment.getProperty("nhn.cloud.keyManager.redisKey")).willReturn(keyid);

		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", Map.of("secret", "{\"host\":\"localhost\",\"database\":0,\"password\":\"password\",\"port\":6379}"));
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
			.willReturn(responseEntity);

		// when
		RedisConfigDto redisConfig = KeyManagerUtil.getRedisConfig(environment, restTemplate);

		// then
		assertNotNull(redisConfig);
		assertEquals("localhost", redisConfig.host());
		assertEquals(0, redisConfig.database());
		assertEquals("password", redisConfig.password());
		assertEquals(6379, redisConfig.port());
	}

	@Test
	void getRabbitMQConfig_returnsRabbitMQConfig() {
		// given
		String keyid = "testRabbitMQKey";
		given(environment.getProperty("nhn.cloud.keyManager.rabbitMQKey")).willReturn(keyid);

		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", Map.of("secret", "{\"host\":\"localhost\",\"port\":5672,\"username\":\"user\",\"password\":\"password\"}"));
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
			.willReturn(responseEntity);

		// when
		RabbitMQConfigDto rabbitMQConfig = KeyManagerUtil.getRabbitMQConfig(environment, restTemplate);

		// then
		assertNotNull(rabbitMQConfig);
		assertEquals("localhost", rabbitMQConfig.host());
		assertEquals(5672, rabbitMQConfig.port());
		assertEquals("user", rabbitMQConfig.username());
		assertEquals("password", rabbitMQConfig.password());
	}

	@Test
	void getDataSource_responseEntityNull_throwsException() {
		// given
		given(environment.getProperty("nhn.cloud.keyManager.appkey")).willReturn(APPKEY);
		given(environment.getProperty("nhn.cloud.keyManager.userAccessKey")).willReturn(USER_ACCESS_KEY);
		given(environment.getProperty("nhn.cloud.keyManager.secretAccessKey")).willReturn(SECRET_ACCESS_KEY);

		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
			.willReturn(null);

		// when, then
		assertThrows(NullPointerException.class, () -> KeyManagerUtil.getDataSource(environment, "testKeyId", restTemplate));
	}

	@Test
	void getDataSource_bodyObjNull_throwsException() {
		// given
		given(environment.getProperty("nhn.cloud.keyManager.appkey")).willReturn(APPKEY);
		given(environment.getProperty("nhn.cloud.keyManager.userAccessKey")).willReturn(USER_ACCESS_KEY);
		given(environment.getProperty("nhn.cloud.keyManager.secretAccessKey")).willReturn(SECRET_ACCESS_KEY);

		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
			.willReturn(responseEntity);

		// when, then
		assertThrows(KeyManagerException.class, () -> KeyManagerUtil.getDataSource(environment, "testKeyId", restTemplate));
	}

	@Test
	void getDataSource_secretKeyMissing_throwsException() {
		// given
		given(environment.getProperty("nhn.cloud.keyManager.appkey")).willReturn(APPKEY);
		given(environment.getProperty("nhn.cloud.keyManager.userAccessKey")).willReturn(USER_ACCESS_KEY);
		given(environment.getProperty("nhn.cloud.keyManager.secretAccessKey")).willReturn(SECRET_ACCESS_KEY);

		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", null);

		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
			.willReturn(responseEntity);

		// when, then
		assertThrows(KeyManagerException.class, () -> KeyManagerUtil.getDataSource(environment, "testKeyId", restTemplate));
	}

	@Test
	void getRedisConfig_jsonProcessingException_throwsException() {
		// given
		String redisKey = "testRedisKey";
		given(environment.getProperty("nhn.cloud.keyManager.redisKey")).willReturn(redisKey);

		// 가짜 응답 데이터 설정 - 잘못된 JSON 형식
		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", Map.of("secret", "{\"host\":\"localhost\",\"port\":\"invalid_port\",\"username\":\"user\",\"password\":\"password\"}"));
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
			.willReturn(responseEntity);

		// when, then
		assertThrows(KeyManagerException.class, () -> KeyManagerUtil.getRedisConfig(environment, restTemplate));
	}

	@Test
	void getDatabaseConfig_jsonProcessingException_throwsException() {
		// given
		String databaseKey = "testDatabaseKey";
		given(environment.getProperty("nhn.cloud.keyManager.couponKey")).willReturn(databaseKey);

		// 가짜 응답 데이터 설정 - 잘못된 JSON 형식
		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", Map.of("secret", "{\"host\":\"localhost\",\"port\":\"invalid_port\",\"username\":\"user\",\"password\":\"password\"}"));
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
			.willReturn(responseEntity);

		// when, then
		assertThrows(KeyManagerException.class, () -> KeyManagerUtil.getDatabaseConfig(environment, restTemplate));
	}

	@Test
	void getRabbitMQConfig_jsonProcessingException_throwsException() {
		// given
		String rabbitMQKey = "testRabbitMQKey";
		given(environment.getProperty("nhn.cloud.keyManager.rabbitMQKey")).willReturn(rabbitMQKey);

		// 가짜 응답 데이터 설정 - 잘못된 JSON 형식
		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", Map.of("secret", "{\"host\":\"localhost\",\"port\":\"invalid_port\",\"username\":\"user\",\"password\":\"password\"}"));
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
			.willReturn(responseEntity);

		// when, then
		assertThrows(KeyManagerException.class, () -> KeyManagerUtil.getRabbitMQConfig(environment, restTemplate));
	}

	@Test
	void getRabbitMQConfig_secretIsNull_throwsException() {
		// given
		String rabbitMQKey = "testRabbitMQKey";
		given(environment.getProperty("nhn.cloud.keyManager.rabbitMQKey")).willReturn(rabbitMQKey);

		// 가짜 응답 데이터 설정 - secret 값이 null
		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", null);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
			.willReturn(responseEntity);

		// when, then
		assertThrows(KeyManagerException.class, () -> KeyManagerUtil.getRabbitMQConfig(environment, restTemplate));
	}

	@Test
	void getRabbitMQConfig_secretIsEmpty_throwsException() {
		// given
		String rabbitMQKey = "testRabbitMQKey";
		given(environment.getProperty("nhn.cloud.keyManager.rabbitMQKey")).willReturn(rabbitMQKey);

		// 가짜 응답 데이터 설정 - secret 값이 빈 문자열
		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", Map.of("secret", ""));
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(responseEntity);

		// when, then
		assertThrows(KeyManagerException.class, () -> KeyManagerUtil.getRabbitMQConfig(environment, restTemplate));
	}
}
