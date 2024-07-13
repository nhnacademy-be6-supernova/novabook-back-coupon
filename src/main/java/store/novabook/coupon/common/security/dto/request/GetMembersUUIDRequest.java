package store.novabook.coupon.common.security.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 회원 UUID 요청 DTO 클래스.
 *
 * @param uuid 회원의 UUID
 */
public record GetMembersUUIDRequest(
	@NotBlank
	String uuid
) {
}