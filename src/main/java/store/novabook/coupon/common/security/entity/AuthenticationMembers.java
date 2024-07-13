package store.novabook.coupon.common.security.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 인증된 회원 정보를 나타내는 엔티티 클래스.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationMembers {
	private long membersId;
	private String loginId;
	private String loginPassword;
	private String role;

	@Builder
	private AuthenticationMembers(long membersId, String loginId, String loginPassword, String role) {
		this.membersId = membersId;
		this.loginId = loginId;
		this.loginPassword = loginPassword;
		this.role = role;
	}

	public static AuthenticationMembers of(long membersId, String loginId, String loginPassword, String role) {
		return AuthenticationMembers.builder()
			.membersId(membersId)
			.loginId(loginId)
			.loginPassword(loginPassword)
			.role(role)
			.build();
	}
}
