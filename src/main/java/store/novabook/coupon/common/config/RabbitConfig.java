package store.novabook.coupon.common.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 설정 클래스.
 * RabbitMQ와 관련된 큐, 익스체인지, 바인딩 및 템플릿을 설정합니다.
 */
@EnableRabbit
@Configuration
public class RabbitConfig {

	@Value("${spring.rabbitmq.host}")
	private String rabbitmqHost;

	@Value("${spring.rabbitmq.port}")
	private int rabbitmqPort;

	@Value("${spring.rabbitmq.username}")
	private String rabbitmqUsername;

	@Value("${spring.rabbitmq.password}")
	private String rabbitmqPassword;

	@Value("${rabbitmq.queue.coupon}")
	private String couponQueueName;

	@Value("${rabbitmq.queue.dead}")
	private String deadQueueName;

	@Value("${rabbitmq.queue.member-coupon}")
	private String memberCouponQueueName;

	@Value("${rabbitmq.exchange.member}")
	private String memberExchangeName;

	@Value("${rabbitmq.exchange.coupon}")
	private String couponExchangeName;

	@Value("${rabbitmq.routing.member}")
	private String memberRoutingKey;

	@Value("${rabbitmq.routing.couponCreated}")
	private String couponCreatedRoutingKey;

	/**
	 * 쿠폰 큐를 생성합니다.
	 *
	 * @return 쿠폰 큐
	 */
	@Bean
	public Queue couponQueue() {
		Map<String, Object> args = new HashMap<>();
		args.put("x-dead-letter-exchange", memberExchangeName);
		args.put("x-dead-letter-routing-key", deadQueueName);
		args.put("x-original-queue", couponQueueName);
		return new Queue(couponQueueName, true, false, false, args);
	}

	/**
	 * 멤버 쿠폰 큐를 생성합니다.
	 *
	 * @return 멤버 쿠폰 큐
	 */
	@Bean
	public Queue memberCouponQueue() {
		Map<String, Object> args = new HashMap<>();
		args.put("x-dead-letter-exchange", couponExchangeName);
		args.put("x-dead-letter-routing-key", deadQueueName);
		args.put("x-original-queue", memberCouponQueueName);
		return new Queue(memberCouponQueueName, true, false, false, args);
	}

	/**
	 * 데드 레터 큐를 생성합니다.
	 *
	 * @return 데드 레터 큐
	 */
	@Bean
	public Queue deadQueue() {
		return new Queue(deadQueueName, true, false, false);
	}

	/**
	 * 멤버 익스체인지를 생성합니다.
	 *
	 * @return 멤버 익스체인지
	 */
	@Bean
	public DirectExchange memberExchange() {
		return new DirectExchange(memberExchangeName);
	}

	/**
	 * 쿠폰 익스체인지를 생성합니다.
	 *
	 * @return 쿠폰 익스체인지
	 */
	@Bean
	public DirectExchange couponExchange() {
		return new DirectExchange(couponExchangeName);
	}

	/**
	 * 쿠폰 큐와 멤버 익스체인지를 바인딩합니다.
	 *
	 * @return 바인딩
	 */
	@Bean
	public Binding couponBinding() {
		return BindingBuilder.bind(couponQueue()).to(memberExchange()).with(memberRoutingKey);
	}

	/**
	 * 멤버 쿠폰 큐와 쿠폰 익스체인지를 바인딩합니다.
	 *
	 * @return 바인딩
	 */
	@Bean
	public Binding couponCreatedBinding() {
		return BindingBuilder.bind(memberCouponQueue()).to(couponExchange()).with(couponCreatedRoutingKey);
	}

	/**
	 * 데드 레터 큐와 쿠폰 익스체인지를 바인딩합니다.
	 *
	 * @return 바인딩
	 */
	@Bean
	public Binding deadBinding() {
		return BindingBuilder.bind(deadQueue()).to(couponExchange()).with(deadQueueName);
	}

	/**
	 * RabbitMQ 연결 팩토리를 생성합니다.
	 *
	 * @return 연결 팩토리
	 */
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(rabbitmqHost);
		connectionFactory.setPort(rabbitmqPort);
		connectionFactory.setUsername(rabbitmqUsername);
		connectionFactory.setPassword(rabbitmqPassword);
		return connectionFactory;
	}

	/**
	 * RabbitTemplate을 생성합니다.
	 *
	 * @param connectionFactory 연결 팩토리
	 * @return RabbitTemplate
	 */
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
		return rabbitTemplate;
	}

	/**
	 * Jackson을 사용한 메시지 컨버터를 생성합니다.
	 *
	 * @return 메시지 컨버터
	 */
	@Bean
	public MessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
