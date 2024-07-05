package store.novabook.coupon.common.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
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
public class RabbitMQConfig {

	@Value("${rabbitmq.queue.couponCreateNormal}")
	private String couponCreateNormalQueue;

	@Value("${rabbitmq.queue.couponCreateHighTraffic}")
	private String couponCreateHighTrafficQueue;

	@Value("${rabbitmq.queue.couponRegisterNormal}")
	private String couponRegisterNormalQueue;

	@Value("${rabbitmq.queue.couponRegisterHighTraffic}")
	private String couponRegisterHighTrafficQueue;

	@Value("${rabbitmq.queue.deadLetter}")
	private String deadLetterQueue;

	@Value("${rabbitmq.exchange.couponOperation}")
	private String couponOperationExchange;

	@Value("${rabbitmq.exchange.deadLetter}")
	private String deadLetterExchange;

	@Value("${rabbitmq.routing.couponCreateNormal}")
	private String couponCreateNormalRoutingKey;

	@Value("${rabbitmq.routing.couponCreateHighTraffic}")
	private String couponCreateHighTrafficRoutingKey;

	@Value("${rabbitmq.routing.couponRegisterNormal}")
	private String couponRegisterNormalRoutingKey;

	@Value("${rabbitmq.routing.couponRegisterHighTraffic}")
	private String couponRegisterHighTrafficRoutingKey;

	/**
	 * couponOperationExchange 빈을 생성합니다.
	 *
	 * @return TopicExchange 객체
	 */
	@Bean
	public TopicExchange couponOperationExchange() {
		return new TopicExchange(couponOperationExchange);
	}

	/**
	 * deadLetterExchange 빈을 생성합니다.
	 *
	 * @return DirectExchange 객체
	 */
	@Bean
	public DirectExchange deadLetterExchange() {
		return new DirectExchange(deadLetterExchange);
	}

	/**
	 * couponCreateNormalQueue 빈을 생성합니다.
	 *
	 * @return Queue 객체
	 */
	@Bean
	public Queue couponCreateNormalQueue() {
		return new Queue(couponCreateNormalQueue, true, false, false, queueArguments(couponCreateNormalQueue));
	}

	/**
	 * couponCreateHighTrafficQueue 빈을 생성합니다.
	 *
	 * @return Queue 객체
	 */
	@Bean
	public Queue couponCreateHighTrafficQueue() {
		return new Queue(couponCreateHighTrafficQueue, true, false, false,
			queueArguments(couponCreateHighTrafficQueue));
	}

	/**
	 * couponRegisterNormalQueue 빈을 생성합니다.
	 *
	 * @return Queue 객체
	 */
	@Bean
	public Queue couponRegisterNormalQueue() {
		return new Queue(couponRegisterNormalQueue, true, false, false, queueArguments(couponRegisterNormalQueue));
	}

	/**
	 * couponRegisterHighTrafficQueue 빈을 생성합니다.
	 *
	 * @return Queue 객체
	 */
	@Bean
	public Queue couponRegisterHighTrafficQueue() {
		return new Queue(couponRegisterHighTrafficQueue, true, false, false,
			queueArguments(couponRegisterHighTrafficQueue));
	}

	/**
	 * deadLetterQueue 빈을 생성합니다.
	 *
	 * @return Queue 객체
	 */
	@Bean
	public Queue deadLetterQueue() {
		return new Queue(deadLetterQueue, true);
	}

	/**
	 * createCouponNormalBinding 빈을 생성합니다.
	 *
	 * @param couponCreateNormalQueue 큐
	 * @param couponOperationExchange 토픽 익스체인지
	 * @return Binding 객체
	 */
	@Bean
	public Binding createCouponNormalBinding(Queue couponCreateNormalQueue, TopicExchange couponOperationExchange) {
		return BindingBuilder.bind(couponCreateNormalQueue)
			.to(couponOperationExchange)
			.with(couponCreateNormalRoutingKey);
	}

	/**
	 * createCouponHighTrafficBinding 빈을 생성합니다.
	 *
	 * @param couponCreateHighTrafficQueue 큐
	 * @param couponOperationExchange      토픽 익스체인지
	 * @return Binding 객체
	 */
	@Bean
	public Binding createCouponHighTrafficBinding(Queue couponCreateHighTrafficQueue,
		TopicExchange couponOperationExchange) {
		return BindingBuilder.bind(couponCreateHighTrafficQueue)
			.to(couponOperationExchange)
			.with(couponCreateHighTrafficRoutingKey);
	}

	/**
	 * registerCouponNormalBinding 빈을 생성합니다.
	 *
	 * @param couponRegisterNormalQueue 큐
	 * @param couponOperationExchange   토픽 익스체인지
	 * @return Binding 객체
	 */
	@Bean
	public Binding registerCouponNormalBinding(Queue couponRegisterNormalQueue, TopicExchange couponOperationExchange) {
		return BindingBuilder.bind(couponRegisterNormalQueue)
			.to(couponOperationExchange)
			.with(couponRegisterNormalRoutingKey);
	}

	/**
	 * registerCouponHighTrafficBinding 빈을 생성합니다.
	 *
	 * @param couponRegisterHighTrafficQueue 큐
	 * @param couponOperationExchange        토픽 익스체인지
	 * @return Binding 객체
	 */
	@Bean
	public Binding registerCouponHighTrafficBinding(Queue couponRegisterHighTrafficQueue,
		TopicExchange couponOperationExchange) {
		return BindingBuilder.bind(couponRegisterHighTrafficQueue)
			.to(couponOperationExchange)
			.with(couponRegisterHighTrafficRoutingKey);
	}

	/**
	 * deadLetterBinding 빈을 생성합니다.
	 *
	 * @param deadLetterQueue    큐
	 * @param deadLetterExchange 토픽 익스체인지
	 * @return Binding 객체
	 */
	@Bean
	public Binding deadLetterBinding(Queue deadLetterQueue, TopicExchange deadLetterExchange) {
		return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with("#");
	}

	/**
	 * RabbitTemplate 빈을 생성합니다.
	 *
	 * @param connectionFactory 커넥션 팩토리
	 * @return RabbitTemplate 객체
	 */
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
		return rabbitTemplate;
	}

	/**
	 * Jackson2JsonMessageConverter 빈을 생성합니다.
	 *
	 * @return MessageConverter 객체
	 */
	@Bean
	public MessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	/**
	 * 큐에 대한 추가 인자를 설정합니다.
	 *
	 * @param queueName 큐 이름
	 * @return 큐 인자 맵
	 */
	private Map<String, Object> queueArguments(String queueName) {
		Map<String, Object> args = new HashMap<>();
		args.put("x-dead-letter-exchange", deadLetterExchange);
		args.put("x-dead-letter-routing-key", deadLetterQueue);
		args.put("x-original-queue", queueName);
		args.put("x-queue-type", "classic");
		return args;
	}
}
