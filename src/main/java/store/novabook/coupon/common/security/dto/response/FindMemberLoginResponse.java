package store.novabook.coupon.common.security.dto.response;

public record FindMemberLoginResponse(
	long id,
	String loginId,
	String password,
	String role) {
}
