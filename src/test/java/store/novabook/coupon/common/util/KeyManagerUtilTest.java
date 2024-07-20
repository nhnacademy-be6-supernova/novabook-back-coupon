package store.novabook.coupon.common.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.KeyManagerException;

class KeyManagerUtilTest {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	@Mock
	private Environment environment;
	@Mock
	private RestTemplate restTemplate;
	@Mock
	private ResponseEntity<Map<String, Object>> responseEntity;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetDataSourceThrowsExceptionWhenBodyIsNull() {
		String keyid = "couponKey";

		Map<String, Object> responseBody = new HashMap<>();

		when(environment.getProperty("nhn.cloud.keyManager.couponKey")).thenReturn(keyid);
		when(environment.getProperty("nhn.cloud.keyManager.appkey")).thenReturn("appkey");
		when(environment.getProperty("nhn.cloud.keyManager.userAccessKey")).thenReturn("userAccessKey");
		when(environment.getProperty("nhn.cloud.keyManager.secretAccessKey")).thenReturn("secretAccessKey");
		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.thenReturn(responseEntity);
		when(responseEntity.getBody()).thenReturn(responseBody);

		KeyManagerException exception = assertThrows(KeyManagerException.class, () -> {
			KeyManagerUtil.getDatabaseConfig(environment);
		});

		assertEquals(ErrorCode.MISSING_BODY_KEY, exception.getErrorCode());
	}

	@Test
	void testGetDataSourceThrowsExceptionWhenResponseIsNot200() {
		String keyid = "couponKey";
		String url = "https://api-keymanager.nhncloudservice.com/keymanager/v1.2/appkey/appkey/secrets/" + keyid;

		when(environment.getProperty("nhn.cloud.keyManager.couponKey")).thenReturn(keyid);
		when(environment.getProperty("nhn.cloud.keyManager.appkey")).thenReturn("appkey");
		when(environment.getProperty("nhn.cloud.keyManager.userAccessKey")).thenReturn("userAccessKey");
		when(environment.getProperty("nhn.cloud.keyManager.secretAccessKey")).thenReturn("secretAccessKey");

		when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

		KeyManagerException exception = assertThrows(KeyManagerException.class, () -> {
			KeyManagerUtil.getDatabaseConfig(environment);
		});

		assertEquals(ErrorCode.MISSING_BODY_KEY, exception.getErrorCode());
	}
}
