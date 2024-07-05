package store.novabook.coupon.common.security.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import store.novabook.coupon.common.security.entity.AuthMembers;

/**
 * 사용자 인증 정보를 담은 UserDetails 구현 클래스.
 */
public class CustomUserDetails implements UserDetails {
	private final transient AuthMembers authMembers;

	/**
	 * CustomUserDetails 생성자.
	 *
	 * @param authMembers 인증된 회원 정보
	 */
	public CustomUserDetails(AuthMembers authMembers) {
		this.authMembers = authMembers;
	}

	/**
	 * 사용자의 권한을 반환합니다.
	 *
	 * @return 권한 컬렉션
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add((GrantedAuthority)authMembers::getRole);

		return collection;
	}

	/**
	 * 사용자 ID를 반환합니다.
	 *
	 * @return 사용자 ID
	 */
	public long getId() {
		return authMembers.getId();
	}

	/**
	 * 사용자의 비밀번호를 반환합니다.
	 *
	 * @return 비밀번호
	 */
	@Override
	public String getPassword() {
		return authMembers.getPassword();
	}

	/**
	 * 사용자의 이름을 반환합니다.
	 *
	 * @return 이름
	 */
	@Override
	public String getUsername() {
		return Long.toString(authMembers.getId());
	}

	/**
	 * 계정이 만료되지 않았는지 여부를 반환합니다.
	 *
	 * @return 만료되지 않음(true)
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 계정이 잠기지 않았는지 여부를 반환합니다.
	 *
	 * @return 잠기지 않음(true)
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 자격 증명이 만료되지 않았는지 여부를 반환합니다.
	 *
	 * @return 만료되지 않음(true)
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 계정이 활성화되었는지 여부를 반환합니다.
	 *
	 * @return 활성화됨(true)
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	/**
	 * 회원의 상세 정보를 반환합니다.
	 *
	 * @return 회원 정보
	 */
	public Object getDetails() {
		return authMembers;
	}
}
