package store.novabook.coupon.common.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

	@Value("${rabbitmq.queue.retry}")
	private String retryQueueName;

	@Value("${rabbitmq.queue.dead}")
	private String deadQueueName;

	@Value("${rabbitmq.exchange.member}")
	private String memberExchangeName;

	@Value("${rabbitmq.routing.member}")
	private String memberRoutingKey;

	@Bean
	public Queue couponQueue() {
		Map<String, Object> args = new HashMap<>();
		args.put("x-dead-letter-exchange", memberExchangeName);
		args.put("x-dead-letter-routing-key", retryQueueName);
		return new Queue(couponQueueName, false, false, true, args);
	}

	@Bean
	public Queue retryQueue() {
		Map<String, Object> args = new HashMap<>();
		args.put("x-dead-letter-exchange", memberExchangeName);
		args.put("x-dead-letter-routing-key", deadQueueName);
		args.put("x-message-ttl", 60000); // 60초 후에 메시지를 이동
		return new Queue(retryQueueName, false, false, true, args);
	}

	@Bean
	public Queue deadQueue() {
		return new Queue(deadQueueName, false, false, true);
	}

	@Bean
	public TopicExchange memberExchange() {
		return new TopicExchange(memberExchangeName);
	}

	@Bean
	public Binding couponBinding() {
		return BindingBuilder.bind(couponQueue()).to(memberExchange()).with(memberRoutingKey);
	}

	@Bean
	public Binding retryBinding() {
		return BindingBuilder.bind(retryQueue()).to(memberExchange()).with(retryQueueName);
	}

	@Bean
	public Binding deadBinding() {
		return BindingBuilder.bind(deadQueue()).to(memberExchange()).with(deadQueueName);
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(rabbitmqHost);
		connectionFactory.setPort(rabbitmqPort);
		connectionFactory.setUsername(rabbitmqUsername);
		connectionFactory.setPassword(rabbitmqPassword);
		return connectionFactory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public MessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
