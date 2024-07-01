package store.novabook.coupon.coupon.dto.request;

import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * {@code GetCouponAllRequest} 레코드는 모든 쿠폰 조회 요청을 나타냅니다.
 *
 * @param couponIdList   쿠폰 ID 리스트
 * @param bookIdList     도서 ID 세트
 * @param categoryIdList 카테고리 ID 세트
 */
@Builder
public record GetCouponAllRequest(@NotNull(message = "쿠폰 번호는 필수 입력 항목입니다.") List<Long> couponIdList,
								  @NotNull(message = "도서 아이디는 필수 입력 항목입니다.") Set<Long> bookIdList,
								  @NotNull(message = "카테고리 아이디는 필수 입력 항목입니다.") Set<Long> categoryIdList) {
}
