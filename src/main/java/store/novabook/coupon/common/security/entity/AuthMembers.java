package store.novabook.coupon.common.security.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthMembers {
	private long id;

	private String username;
	private String password;

	private String role;
}
