package store.novabook.coupon.common.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * {@code StoreAdapter} 인터페이스는 외부의 스토어 서비스와의 통신을 위한 Feign 클라이언트를 정의합니다.
 */
@FeignClient(name = "store-service")
public interface StoreAdapter {

	/**
	 * 주어진 책 ID를 이용하여 책 이름을 조회합니다.
	 *
	 * @param bookId 책 ID
	 * @return 책 이름
	 */
	@GetMapping("/books/{bookId}")
	String getBookName(@PathVariable("bookId") Long bookId);
}
