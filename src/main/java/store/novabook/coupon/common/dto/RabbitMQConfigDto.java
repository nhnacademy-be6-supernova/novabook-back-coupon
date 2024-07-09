package store.novabook.coupon.common.dto;

public record RabbitMQConfigDto(
	String host,
	int port,
	String username,
	String password
) {
}
