package store.novabook.coupon.common.security.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 인증된 회원 정보를 나타내는 엔티티 클래스입니다.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationMembers {

	/**
	 * 회원 ID
	 */
	private long membersId;

	/**
	 * 로그인 ID
	 */
	private String loginId;

	/**
	 * 로그인 비밀번호
	 */
	private String loginPassword;

	/**
	 * 회원 역할
	 */
	private String role;

	/**
	 * {@code AuthenticationMembers}의 빌더를 사용하여 인스턴스를 생성합니다.
	 *
	 * @param membersId     회원 ID
	 * @param loginId       로그인 ID
	 * @param loginPassword 로그인 비밀번호
	 * @param role          회원 역할
	 */
	@Builder
	private AuthenticationMembers(long membersId, String loginId, String loginPassword, String role) {
		this.membersId = membersId;
		this.loginId = loginId;
		this.loginPassword = loginPassword;
		this.role = role;
	}

	/**
	 * 정적 팩토리 메서드를 사용하여 {@code AuthenticationMembers}의 인스턴스를 생성합니다.
	 *
	 * @param membersId     회원 ID
	 * @param loginId       로그인 ID
	 * @param loginPassword 로그인 비밀번호
	 * @param role          회원 역할
	 * @return {@code AuthenticationMembers}의 새 인스턴스
	 */
	public static AuthenticationMembers of(long membersId, String loginId, String loginPassword, String role) {
		return AuthenticationMembers.builder()
			.membersId(membersId)
			.loginId(loginId)
			.loginPassword(loginPassword)
			.role(role)
			.build();
	}
}
