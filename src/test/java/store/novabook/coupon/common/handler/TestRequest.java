package store.novabook.coupon.common.handler;

import jakarta.validation.constraints.NotBlank;

class TestRequest {
	@NotBlank
	private String name;
}
