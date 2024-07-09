package store.novabook.coupon.common.messaging.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@JsonDeserialize(builder = OrderSagaMessage.OrderSagaMessageBuilder.class)
public class OrderSagaMessage {
	long calculateTotalAmount;
	boolean noUsePoint;
	boolean noUseCoupon;
	String status;
	PaymentRequest paymentRequest;
	@JsonPOJOBuilder(withPrefix = "")
	public static class OrderSagaMessageBuilder {
	}
}
