package store.novabook.coupon.common.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import store.novabook.coupon.common.dto.ApiResponse;
import store.novabook.coupon.common.security.dto.CustomUserDetails;
import store.novabook.coupon.common.security.dto.request.FindMembersRequest;
import store.novabook.coupon.common.security.dto.response.FindMemberLoginResponse;
import store.novabook.coupon.common.security.entity.Users;
@Service
public class CustomAdminDetailsService implements UserDetailsService {

	private final CustomMembersDetailClient customMembersDetailClient;

	public CustomAdminDetailsService(CustomMembersDetailClient customMembersDetailClient) {
		this.customMembersDetailClient = customMembersDetailClient;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		FindMembersRequest findMembersRequest = new FindMembersRequest(username);

		ApiResponse<FindMemberLoginResponse> findMemberLoginResponseResponse = customMembersDetailClient.findAdmin(
			findMembersRequest);

		Users users = new Users();
		users.setId(findMemberLoginResponseResponse.getBody().id());
		users.setUsername(findMemberLoginResponseResponse.getBody().loginId());
		users.setPassword(findMemberLoginResponseResponse.getBody().password());
		users.setRole(findMemberLoginResponseResponse.getBody().role());
		return new CustomUserDetails(users);
	}
}