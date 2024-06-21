package store.novabook.coupon.common.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.domain.CouponType;
import store.novabook.coupon.coupon.repository.CouponRepository;

/**
 * 쿠폰 코드를 생성하는 유틸리티 클래스입니다. 이 클래스는 주어진 쿠폰 유형에 따라 고유한 쿠폰 코드를 생성합니다.
 * 쿠폰 코드는 16자의 대문자 알파벳과 숫자로 구성됩니다.
 *
 * <p> 이 클래스는 {@link CouponRepository}를 사용하여 생성된 코드의 고유성을 보장합니다.
 * 이미 존재하는 코드가 생성될 경우, 새로운 고유 코드를 생성할 때까지 반복합니다.
 * </p>
 *
 * <p> 예제 사용법:
 * <pre>
 *     CouponType couponType = ...;
 *     String uniqueCode = couponCodeGenerator.generateUniqueCode(couponType);
 * </pre>
 * </p>
 */
@Component
@RequiredArgsConstructor
public class CouponCodeGenerator {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int CODE_LENGTH = 16;
	private static final SecureRandom random = new SecureRandom();

	private final CouponRepository couponRepository;

	/**
	 * 주어진 쿠폰 유형에 대해 고유한 쿠폰 코드를 생성합니다.
	 *
	 * @param couponType 쿠폰 유형
	 * @return 고유한 쿠폰 코드
	 */
	@Transactional(readOnly = true)
	public String generateUniqueCode(CouponType couponType) {
		String code;
		do {
			code = generateCode(couponType);
		} while (couponRepository.existsById(code));
		return code;
	}

	/**
	 * 주어진 쿠폰 유형의 접두사와 함께 새로운 쿠폰 코드를 생성합니다.
	 *
	 * @param couponType 쿠폰 유형
	 * @return 새로운 쿠폰 코드
	 */
	private String generateCode(CouponType couponType) {
		StringBuilder code = new StringBuilder(CODE_LENGTH);
		code.append(couponType.getPrefix());

		for (int i = 1; i < CODE_LENGTH; i++) {
			code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
		}

		return code.toString();
	}
}
