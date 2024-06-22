package store.novabook.coupon.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

	@Value("${rabbitmq.exchange.member}")
	private String memberExchangeName;

	@Value("${rabbitmq.routing.member}")
	private String memberRoutingKey;

	@Bean
	public Queue couponQueue() {
		return new Queue(couponQueueName);
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
