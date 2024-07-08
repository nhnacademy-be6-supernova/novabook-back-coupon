package store.novabook.coupon.common.dto;

public record RedisConfigDto (
	String host,
	int database,
	String password,
	int port
) {
}
