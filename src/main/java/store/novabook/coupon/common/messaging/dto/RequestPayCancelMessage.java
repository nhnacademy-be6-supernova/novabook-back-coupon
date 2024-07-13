package store.novabook.coupon.common.messaging.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class RequestPayCancelMessage {
	Long couponId;
	Long usePointAmount;
	Long memberId;
	Long earnPointAmoun;
	String status;
}
