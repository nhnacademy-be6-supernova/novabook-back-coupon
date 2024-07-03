package store.novabook.coupon.common.security.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FindMemberRequest(
	@NotBlank
	String loginId
) {
}
