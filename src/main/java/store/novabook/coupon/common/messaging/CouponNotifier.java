package store.novabook.coupon.common.messaging;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 관련 알림을 Redis를 통해 전송하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
public class CouponNotifier {

	private final RedisTemplate<String, String> redisTemplate;
	private final ChannelTopic topic;

	/**
	 * 클라이언트에게 알림 메시지를 전송합니다.
	 *
	 * @param clientId 클라이언트 ID
	 * @param message  전송할 메시지
	 */
	public void notify(String clientId, String message) {
		String notificationMessage = clientId + ":" + message;
		redisTemplate.convertAndSend(topic.getTopic(), notificationMessage);
	}
}
