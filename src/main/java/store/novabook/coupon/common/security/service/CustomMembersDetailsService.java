package store.novabook.coupon.common.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import store.novabook.coupon.common.dto.ApiResponse;
import store.novabook.coupon.common.security.dto.request.FindMembersRequest;
import store.novabook.coupon.common.security.dto.CustomUserDetails;
import store.novabook.coupon.common.security.dto.response.FindMemberLoginResponse;
import store.novabook.coupon.common.security.entity.Users;

@Service
public class CustomMembersDetailsService implements UserDetailsService {

	private final CustomMembersDetailClient customMembersDetailClient;

	public CustomMembersDetailsService(CustomMembersDetailClient customMembersDetailClient) {
		this.customMembersDetailClient = customMembersDetailClient;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		FindMembersRequest findMembersRequest = new FindMembersRequest(username);

		ApiResponse<FindMemberLoginResponse> findMembersLoginResponseResponse = customMembersDetailClient.find(
			findMembersRequest);

		Users users = new Users();
		users.setId(findMembersLoginResponseResponse.getBody().id());
		users.setUsername(findMembersLoginResponseResponse.getBody().loginId());
		users.setPassword(findMembersLoginResponseResponse.getBody().password());
		users.setRole(findMembersLoginResponseResponse.getBody().role());
		return new CustomUserDetails(users);
	}
}