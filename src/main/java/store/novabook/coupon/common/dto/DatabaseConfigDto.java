package store.novabook.coupon.common.dto;

public record DatabaseConfigDto(
	String url,
	String username,
	String password
) {
}
