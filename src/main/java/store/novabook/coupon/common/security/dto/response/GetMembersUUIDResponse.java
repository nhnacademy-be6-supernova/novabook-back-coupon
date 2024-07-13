package store.novabook.coupon.common.security.dto.response;

/**
 * 회원 UUID 응답 DTO 클래스.
 *
 * @param membersId 회원 ID
 */
public record GetMembersUUIDResponse(
	Long membersId,
	String role
) {
}

