package store.novabook.coupon.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 설정 클래스.
 * Redis와 관련된 연결 팩토리, 템플릿 및 채널 주제를 설정합니다.
 */
@Configuration
@EnableRedisRepositories
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	@Value("${spring.data.redis.password}")
	private String password;

	@Value("${spring.data.redis.database}")
	private int database;

	/**
	 * Redis 연결 팩토리 빈을 생성합니다.
	 *
	 * @return RedisConnectionFactory 객체
	 */
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		redisStandaloneConfiguration.setPassword(password);
		redisStandaloneConfiguration.setDatabase(database);
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	/**
	 * RedisTemplate 빈을 생성합니다.
	 *
	 * @return RedisTemplate 객체
	 */
	@Bean
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		return redisTemplate;
	}

	/**
	 * Redis 채널 주제 빈을 생성합니다.
	 *
	 * @return ChannelTopic 객체
	 */
	@Bean
	public ChannelTopic topic() {
		return new ChannelTopic("notificationTopic");
	}
}
