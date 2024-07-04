package store.novabook.coupon.common.security.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import store.novabook.coupon.common.security.entity.AuthMembers;

public class CustomUserDetails implements UserDetails {
	private final transient AuthMembers authMembers;

	public CustomUserDetails(AuthMembers authMembers) {
		this.authMembers = authMembers;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add((GrantedAuthority)authMembers::getRole);

		return collection;
	}

	public long getId() {
		return authMembers.getId();
	}

	@Override
	public String getPassword() {
		return authMembers.getPassword();
	}

	@Override
	public String getUsername() {
		return Long.toString(authMembers.getId());
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Object getDetails() {
		return authMembers;
	}
}
