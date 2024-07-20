package store.novabook.coupon.common.dto;

/**
 * 데이터베이스 구성 정보를 담는 데이터 전송 객체(DTO)입니다.
 *
 * @param url      데이터베이스 URL
 * @param username 데이터베이스 사용자 이름
 * @param password 데이터베이스 비밀번호
 */
public record DatabaseConfigDto(
	String url,
	String username,
	String password
) {
}
