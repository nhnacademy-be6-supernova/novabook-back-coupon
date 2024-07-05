package store.novabook.coupon.common.security.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 인증된 회원 정보를 나타내는 엔티티 클래스.
 */
@Getter
@Setter
public class AuthMembers {
	private long id;
	private String username;
	private String password;
	private String role;
}
