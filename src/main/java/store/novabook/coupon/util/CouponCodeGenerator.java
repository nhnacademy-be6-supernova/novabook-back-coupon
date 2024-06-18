package store.novabook.coupon.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.domain.CouponType;
import store.novabook.coupon.coupon.repository.CouponRepository;

@Component
@RequiredArgsConstructor
public class CouponCodeGenerator {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int CODE_LENGTH = 16;
	private static final SecureRandom random = new SecureRandom();

	private final CouponRepository couponRepository;

	public String generateUniqueCode(CouponType couponType) {
		String code;
		do {
			code = generateCode(couponType);
		} while (couponRepository.existsById(code));
		return code;
	}

	private String generateCode(CouponType couponType) {
		StringBuilder code = new StringBuilder(CODE_LENGTH);
		code.append(couponType.getPrefix());

		for (int i = 1; i < CODE_LENGTH; i++) {
			code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
		}

		return code.toString();
	}

}
