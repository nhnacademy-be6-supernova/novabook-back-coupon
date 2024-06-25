package store.novabook.coupon.common.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import store.novabook.coupon.common.adapter.dto.GetMemberIdAllResponse;
import store.novabook.coupon.common.adapter.dto.GetMemberIdWithBirthdayRequest;

@FeignClient(name = "store-service", url = "${store.service.url}")
public interface StoreAdapter {

	@GetMapping("/members/birthdays")
	GetMemberIdAllResponse getMemberAllWithBirthdays(GetMemberIdWithBirthdayRequest request);

	@GetMapping("/books/{bookId}/name")
	String getBookName(@PathVariable("bookId") Long bookId);
}
