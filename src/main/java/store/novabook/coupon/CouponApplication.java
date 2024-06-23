package store.novabook.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class CouponApplication {
	public static void main(String[] args) {
		SpringApplication.run(CouponApplication.class, args);
	}

}
