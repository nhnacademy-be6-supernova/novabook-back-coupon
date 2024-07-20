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

/**
 * Orders 서비스에 대한 RabbitMQ 설정 클래스입니다.
 */
@Configuration
public class OrdersRabbitMQConfig {

	/**
	 * 사가(Saga)를 위한 direct exchange를 생성합니다.
	 *
	 * @return 생성된 exchange
	 */
	@Bean
	public Exchange sagaExchange() {
		return ExchangeBuilder.directExchange("nova.orders.saga.exchange").build();
	}

	/**
	 * Jackson2JsonMessageConverter를 사용하는 메시지 컨버터를 생성합니다.
	 *
	 * @return 메시지 컨버터
	 */
	@Bean
	public MessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}

	/**
	 * 쿠폰 적용을 위한 큐를 생성합니다.
	 *
	 * @return 생성된 큐
	 */
	@Bean
	public Queue ordersApplyCouponQueue() {
		return QueueBuilder.durable("nova.coupon.apply.queue").build();
	}

	/**
	 * 결제 취소 요청을 위한 큐를 생성합니다.
	 *
	 * @return 생성된 큐
	 */
	@Bean
	public Queue requestPayCancelQueue() {
		return QueueBuilder.durable("nova.coupon.request.pay.cancel.queue").build();
	}

	/**
	 * 적용된 쿠폰 보상을 위한 큐를 생성합니다.
	 *
	 * @return 생성된 큐
	 */
	@Bean
	public Queue compensateOrdersApplyCouponQueue() {
		return QueueBuilder.durable("nova.coupon.compensate.apply.queue").build();
	}

	/**
	 * 주문 사가의 데드 레터 큐를 생성합니다.
	 *
	 * @return 생성된 큐
	 */
	@Bean
	public Queue deadOrdersSagaQueue() {
		return QueueBuilder.durable("nova.orders.saga.dead.queue").build();
	}

	/**
	 * API1을 위한 프로듀서 큐를 생성합니다.
	 *
	 * @return 생성된 큐
	 */
	@Bean
	public Queue api1ProducerQueue() {
		return QueueBuilder.durable("nova.api1-producer-queue").build();
	}

	/**
	 * API2를 위한 프로듀서 큐를 생성합니다.
	 *
	 * @return 생성된 큐
	 */
	@Bean
	public Queue api2ProducerQueue() {
		return QueueBuilder.durable("nova.api2-producer-queue").build();
	}

	/**
	 * API3을 위한 프로듀서 큐를 생성합니다.
	 *
	 * @return 생성된 큐
	 */
	@Bean
	public Queue api3ProducerQueue() {
		return QueueBuilder.durable("nova.api3-producer-queue").build();
	}

	/**
	 * 주문 쿠폰 적용 큐를 사가 익스체인지에 라우팅 키로 바인딩합니다.
	 *
	 * @return 바인딩
	 */
	@Bean
	public Binding applyCouponBinding() {
		return BindingBuilder.bind(ordersApplyCouponQueue())
			.to(sagaExchange())
			.with("coupon.apply.routing.key")
			.noargs();
	}

	/**
	 * 결제 취소 요청 큐를 사가 익스체인지에 라우팅 키로 바인딩합니다.
	 *
	 * @return 바인딩
	 */
	@Bean
	public Binding requestPayCancelBinding() {
		return BindingBuilder.bind(requestPayCancelQueue())
			.to(sagaExchange())
			.with("coupon.request.pay.cancel.routing.key")
			.noargs();
	}

	/**
	 * 주문 사가 데드 레터 큐를 사가 익스체인지에 라우팅 키로 바인딩합니다.
	 *
	 * @return 바인딩
	 */
	@Bean
	public Binding deadOrdersSagaBinding() {
		return BindingBuilder.bind(deadOrdersSagaQueue())
			.to(sagaExchange())
			.with("nova.orders.saga.dead.routing.key")
			.noargs();
	}

	/**
	 * 쿠폰 적용 보상 큐를 사가 익스체인지에 라우팅 키로 바인딩합니다.
	 *
	 * @return 바인딩
	 */
	@Bean
	public Binding compensateApplyCouponBinding() {
		return BindingBuilder.bind(compensateOrdersApplyCouponQueue())
			.to(sagaExchange())
			.with("compensate.coupon.apply.routing.key")
			.noargs();
	}

	/**
	 * 주문을 위한 RabbitTemplate을 생성하고, 커스텀 메시지 컨버터를 설정합니다.
	 *
	 * @param connectionFactory 연결 팩토리
	 * @return 생성된 RabbitTemplate
	 */
	@Bean
	public RabbitTemplate ordersRabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(converter());
		return rabbitTemplate;
	}

	/**
	 * RabbitListenerContainerFactory를 생성하고, 커스텀 메시지 컨버터를 설정합니다.
	 *
	 * @param connectionFactory 연결 팩토리
	 * @return 생성된 SimpleRabbitListenerContainerFactory
	 */
	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(converter());
		return factory;
	}

	/**
	 * API1 프로듀서 큐를 사가 익스체인지에 라우팅 키로 바인딩합니다.
	 *
	 * @return 바인딩
	 */
	@Bean
	public Binding api1ProducerBinding() {
		return BindingBuilder.bind(api1ProducerQueue())
			.to(sagaExchange())
			.with("nova.api1-producer-routing-key")
			.noargs();
	}

	/**
	 * API2 프로듀서 큐를 사가 익스체인지에 라우팅 키로 바인딩합니다.
	 *
	 * @return 바인딩
	 */
	@Bean
	public Binding api2ProducerBinding() {
		return BindingBuilder.bind(api2ProducerQueue())
			.to(sagaExchange())
			.with("nova.api2-producer-routing-key")
			.noargs();
	}

	/**
	 * API3 프로듀서 큐를 사가 익스체인지에 라우팅 키로 바인딩합니다.
	 *
	 * @return 바인딩
	 */
	@Bean
	public Binding api3ProducerBinding() {
		return BindingBuilder.bind(api3ProducerQueue())
			.to(sagaExchange())
			.with("nova.api3-producer-routing-key")
			.noargs();
	}
}
