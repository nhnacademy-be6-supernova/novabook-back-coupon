package store.novabook.coupon.common.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import store.novabook.coupon.common.adapter.dto.GetMemberIdAllResponse;
import store.novabook.coupon.common.adapter.dto.GetMemberIdWithBirthdayRequest;

@FeignClient(name = "store-service", url = "${store.service.url}")
public interface StoreApi {

	@GetMapping("/members/birthdays")
	GetMemberIdAllResponse getMemberAllWithBirthdays(GetMemberIdWithBirthdayRequest request);
}