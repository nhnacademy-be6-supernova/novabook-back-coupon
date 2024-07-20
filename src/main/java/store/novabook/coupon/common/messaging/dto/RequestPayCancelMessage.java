package store.novabook.coupon.common.messaging.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 결제 취소 요청 메시지를 담는 클래스입니다.
 */
@Builder
@Setter
@Getter
public class RequestPayCancelMessage {

	/**
	 * 쿠폰 ID
	 */
	Long couponId;

	/**
	 * 사용 포인트 금액
	 */
	Long usePointAmount;

	/**
	 * 회원 ID
	 */
	Long memberId;

	/**
	 * 적립 포인트 금액
	 */
	Long earnPointAmount;

	/**
	 * 상태
	 */
	String status;
}
