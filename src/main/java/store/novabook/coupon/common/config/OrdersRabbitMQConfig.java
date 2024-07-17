package store.novabook.coupon.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrdersRabbitMQConfig {

	@Bean
	public Exchange sagaExchange() {
		return ExchangeBuilder.directExchange("nova.orders.saga.exchange").build();
	}

	@Bean
	public MessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}

	// QUEUES
	@Bean
	public Queue ordersApplyCouponQueue() {
		return QueueBuilder.durable("nova.coupon.apply.queue").build();
	}

	@Bean
	public Queue requestPayCancelQueue() {
		return QueueBuilder.durable("nova.coupon.request.pay.cancel.queue").build();
	}

	/*보상 트랜잭션 큐*/
	@Bean
	public Queue compensateOrdersApplyCouponQueue() {
		return QueueBuilder.durable("nova.coupon.compensate.apply.queue").build();
	}

	// Dead queue
	@Bean
	public Queue deadOrdersSagaQueue() {
		return QueueBuilder.durable("nova.orders.saga.dead.queue").build();
	}

	// SAGA QUEUE
	@Bean
	public Queue api1ProducerQueue() {
		return QueueBuilder.durable("nova.api1-producer-queue").build();
	}

	@Bean
	public Queue api2ProducerQueue() {
		return QueueBuilder.durable("nova.api2-producer-queue").build();
	}

	@Bean
	public Queue api3ProducerQueue() {
		return QueueBuilder.durable("nova.api3-producer-queue").build();
	}

	// BINDING
	@Bean
	public Binding applyCouponBinding() {
		return BindingBuilder.bind(ordersApplyCouponQueue())
			.to(sagaExchange())
			.with("coupon.apply.routing.key")
			.noargs();
	}

	@Bean
	public Binding requestPayCancelBinding() {
		return BindingBuilder.bind(requestPayCancelQueue())
			.to(sagaExchange())
			.with("coupon.request.pay.cancel.routing.key")
			.noargs();
	}

	@Bean
	public Binding deadOrdersSagaBinding() {
		return BindingBuilder.bind(deadOrdersSagaQueue())
			.to(sagaExchange())
			.with("nova.orders.saga.dead.routing.key")
			.noargs();
	}
	// dead queue

	@Bean
	public Binding compensateApplyCouponBinding() {
		return BindingBuilder.bind(compensateOrdersApplyCouponQueue())
			.to(sagaExchange())
			.with("compensate.coupon.apply.routing.key")
			.noargs();
	}

	@Bean
	public RabbitTemplate ordersRabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(converter());
		return rabbitTemplate;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(converter());
		return factory;
	}

	// SAGA BINDING
	@Bean
	public Binding api1ProducerBinding() {
		return BindingBuilder.bind(api1ProducerQueue())
			.to(sagaExchange())
			.with("nova.api1-producer-routing-key")
			.noargs();
	}

	@Bean
	public Binding api2ProducerBinding() {
		return BindingBuilder.bind(api2ProducerQueue())
			.to(sagaExchange())
			.with("nova.api2-producer-routing-key")
			.noargs();
	}

	@Bean
	public Binding api3ProducerBinding() {
		return BindingBuilder.bind(api3ProducerQueue())
			.to(sagaExchange())
			.with("nova.api3-producer-routing-key")
			.noargs();
	}

}
