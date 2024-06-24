package store.novabook.coupon.common.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "batch-service", url = "${batch.service.url}")
public interface BatchAdapter {

	@GetMapping("/api/v1/batch/coupons/general/{couponCode}")
	void startCouponDistribution(@PathVariable String couponCode);
}
