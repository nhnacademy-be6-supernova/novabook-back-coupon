package store.novabook.coupon.common.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "store-service", url = "${store.service.url}")
public interface StoreAdapter {

	@GetMapping("/books/{bookId}")
	String getBookName(@PathVariable("bookId") Long bookId);
}
