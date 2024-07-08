package store.novabook.coupon;

import static org.hibernate.validator.internal.util.Contracts.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import store.novabook.coupon.coupon.repository.CouponRepository;

@SpringBootTest
class CouponApplicationTests {
	@Autowired
	private CouponRepository couponRepository;
	@Test
	void contextLoads() {
		assertNotNull(couponRepository);
	}
}
