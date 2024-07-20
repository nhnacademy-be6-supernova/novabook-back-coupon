package store.novabook.coupon.common.dto;

/**
 * RabbitMQ 구성 정보를 담는 데이터 전송 객체(DTO)입니다.
 *
 * @param host     RabbitMQ 호스트
 * @param port     RabbitMQ 포트 번호
 * @param username RabbitMQ 사용자 이름
 * @param password RabbitMQ 비밀번호
 */
public record RabbitMQConfigDto(
	String host,
	int port,
	String username,
	String password
) {
}
