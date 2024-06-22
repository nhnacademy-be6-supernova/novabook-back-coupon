package store.novabook.coupon.common.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	public static final String MEMBER_QUEUE = "memberQueue";

	@Bean
	public Queue memberQueue() {
		return new Queue(MEMBER_QUEUE, false);
	}
}
