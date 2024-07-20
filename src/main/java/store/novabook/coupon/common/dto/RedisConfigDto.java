package store.novabook.coupon.common.dto;

/**
 * Redis 구성 정보를 담는 데이터 전송 객체(DTO)입니다.
 *
 * @param host     Redis 호스트
 * @param database Redis 데이터베이스 인덱스
 * @param password Redis 비밀번호
 * @param port     Redis 포트 번호
 */
public record RedisConfigDto(
	String host,
	int database,
	String password,
	int port
) {
}
