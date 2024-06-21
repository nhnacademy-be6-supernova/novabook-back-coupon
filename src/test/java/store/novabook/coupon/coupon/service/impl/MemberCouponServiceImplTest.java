package store.novabook.coupon.coupon.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ForbiddenException;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.coupon.domain.BookCoupon;
import store.novabook.coupon.coupon.domain.CategoryCoupon;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.DiscountType;
import store.novabook.coupon.coupon.domain.MemberCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.dto.MemberBookCouponDto;
import store.novabook.coupon.coupon.dto.MemberCategoryCouponDto;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponRequest;
import store.novabook.coupon.coupon.dto.request.PutMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponByTypeResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponResponse;
import store.novabook.coupon.coupon.repository.BookCouponRepository;
import store.novabook.coupon.coupon.repository.CategoryCouponRepository;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.repository.MemberCouponRepository;

class MemberCouponServiceImplTest {

	@InjectMocks
	private MemberCouponServiceImpl memberCouponService;

	@Mock
	private MemberCouponRepository memberCouponRepository;

	@Mock
	private CouponRepository couponRepository;

	@Mock
	private CategoryCouponRepository categoryCouponRepository;

	@Mock
	private BookCouponRepository bookCouponRepository;

	private Coupon validCoupon;
	private MemberCoupon memberCoupon;
	private GetMemberCouponResponse getMemberCouponResponse;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		validCoupon = Coupon.builder()
			.code("VALIDCODE")
			.name("Valid Coupon")
			.discountAmount(1000)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000)
			.minPurchaseAmount(100)
			.startedAt(LocalDateTime.now().minusDays(1))
			.expirationAt(LocalDateTime.now().plusDays(7))
			.build();

		memberCoupon = MemberCoupon.builder()
			.coupon(validCoupon)
			.memberId(1L)
			.status(MemberCouponStatus.UNUSED)
			.build();

		getMemberCouponResponse = GetMemberCouponResponse.builder()
			.memberCouponId(memberCoupon.getId())
			.code(validCoupon.getCode())
			.name(validCoupon.getName())
			.discountAmount(validCoupon.getDiscountAmount())
			.discountType(validCoupon.getDiscountType())
			.maxDiscountAmount(validCoupon.getMaxDiscountAmount())
			.minPurchaseAmount(validCoupon.getMinPurchaseAmount())
			.startedAt(validCoupon.getStartedAt())
			.expirationAt(validCoupon.getExpirationAt())
			.build();
	}

	@Test
	@DisplayName("회원 쿠폰 저장 테스트 - 성공")
	void saveMemberCouponTestSuccess() {
		CreateMemberCouponRequest request = new CreateMemberCouponRequest("VALIDCODE");

		given(couponRepository.findById("VALIDCODE")).willReturn(Optional.of(validCoupon));
		given(memberCouponRepository.save(any(MemberCoupon.class))).willReturn(memberCoupon);

		CreateMemberCouponResponse response = memberCouponService.saveMemberCoupon(1L, request);

		assertThat(response.id()).isEqualTo(memberCoupon.getId());
	}

	@Test
	@DisplayName("회원 쿠폰 저장 테스트 - 실패 (쿠폰 없음)")
	void saveMemberCouponTestFailureCouponNotFound() {
		CreateMemberCouponRequest request = new CreateMemberCouponRequest("INVALIDCODE");

		given(couponRepository.findById("INVALIDCODE")).willReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			memberCouponService.saveMemberCoupon(1L, request);
		});
	}

	@Test
	@DisplayName("회원 쿠폰 저장 테스트 - 실패 (쿠폰 만료됨)")
	void saveMemberCouponTestFailureExpiredCoupon() {
		Coupon expiredCoupon = Coupon.builder()
			.code("EXPIREDCODE")
			.name("Expired Coupon")
			.discountAmount(1000)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000)
			.minPurchaseAmount(100)
			.startedAt(LocalDateTime.now().minusDays(10))
			.expirationAt(LocalDateTime.now().minusDays(1))
			.build();

		CreateMemberCouponRequest request = new CreateMemberCouponRequest("EXPIREDCODE");

		given(couponRepository.findById("EXPIREDCODE")).willReturn(Optional.of(expiredCoupon));

		assertThrows(BadRequestException.class, () -> {
			memberCouponService.saveMemberCoupon(1L, request);
		});
	}

	@Test
	@DisplayName("유효한 회원 쿠폰 조회 테스트 - 성공")
	void getMemberCouponAllByValidTestSuccess() {
		// given
		Coupon coupon = Coupon.builder()
			.code("VALIDCODE")
			.name("Coupon Name")
			.discountAmount(1000L)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now().minusDays(1))
			.expirationAt(LocalDateTime.now().plusDays(1))
			.build();

		GetMemberCouponResponse getMemberCouponResponse = new GetMemberCouponResponse(
			1L, "VALIDCODE", "Coupon Name", 1000L, DiscountType.PERCENT, 5000L, 10000L,
			LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)
		);

		given(memberCouponRepository.findGeneralCouponsByMemberId(any(Long.class), any(Boolean.class)))
			.willReturn(List.of(getMemberCouponResponse));
		given(memberCouponRepository.findBookCouponsByMemberId(any(Long.class), any(Boolean.class)))
			.willReturn(List.of(new MemberBookCouponDto(1L, BookCoupon.builder().coupon(coupon).bookId(1L).build())));
		given(memberCouponRepository.findCategoryCouponsByMemberId(any(Long.class), any(Boolean.class)))
			.willReturn(List.of(
				new MemberCategoryCouponDto(1L, CategoryCoupon.builder().coupon(coupon).categoryId(1L).build())));

		// when
		GetMemberCouponByTypeResponse response = memberCouponService.getMemberCouponAllByValid(1L, true);

		// then
		assertThat(response.generalCouponList()).hasSize(1);
		assertThat(response.generalCouponList().get(0).code()).isEqualTo("VALIDCODE");
		assertThat(response.bookCouponList()).hasSize(1);
		assertThat(response.categoryCouponList()).hasSize(1);
	}

	@Test
	@DisplayName("상태별 회원 쿠폰 조회 테스트 - 성공")
	void getMemberCouponAllByStatusTestSuccess() {
		Page<MemberCoupon> memberCouponPage = new PageImpl<>(List.of(memberCoupon));
		given(memberCouponRepository.findAllByStatus(any(MemberCouponStatus.class), any(PageRequest.class))).willReturn(
			memberCouponPage);

		GetMemberCouponAllResponse response = memberCouponService.getMemberCouponAllByStatus(1L,
			MemberCouponStatus.UNUSED, PageRequest.of(0, 10));

		assertThat(response.memberCouponPage().getContent()).hasSize(1);
		assertThat(response.memberCouponPage().getContent().get(0).code()).isEqualTo("VALIDCODE");
	}

	@Test
	@DisplayName("회원 쿠폰 상태 업데이트 테스트 - 성공")
	void updateMemberCouponStatusTestSuccess() {
		PutMemberCouponRequest request = new PutMemberCouponRequest(1L, MemberCouponStatus.USED);

		given(memberCouponRepository.findById(1L)).willReturn(Optional.of(memberCoupon));
		memberCouponService.updateMemberCouponStatus(1L, request);

		assertThat(memberCoupon.getStatus()).isEqualTo(MemberCouponStatus.USED);
	}

	@Test
	@DisplayName("회원 쿠폰 상태 업데이트 테스트 - 실패 (쿠폰 없음)")
	void updateMemberCouponStatusTestFailureCouponNotFound() {
		PutMemberCouponRequest request = new PutMemberCouponRequest(1L, MemberCouponStatus.USED);

		given(memberCouponRepository.findById(1L)).willReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			memberCouponService.updateMemberCouponStatus(1L, request);
		});
	}

	@Test
	@DisplayName("회원 쿠폰 상태 업데이트 테스트 - 실패 (권한 없음)")
	void updateMemberCouponStatusTestFailureForbidden() {
		PutMemberCouponRequest request = new PutMemberCouponRequest(1L, MemberCouponStatus.USED);
		MemberCoupon otherMemberCoupon = MemberCoupon.builder().coupon(validCoupon).memberId(2L) // 다른 memberId 설정
			.status(MemberCouponStatus.UNUSED).build();

		given(memberCouponRepository.findById(1L)).willReturn(Optional.of(otherMemberCoupon));

		assertThrows(ForbiddenException.class, () -> {
			memberCouponService.updateMemberCouponStatus(1L, request);
		});
	}

	@Test
	@DisplayName("회원 쿠폰 상태 업데이트 테스트 - 실패 (유효하지 않은 쿠폰)")
	void updateMemberCouponStatusTestFailureInvalidCoupon() {
		PutMemberCouponRequest request = new PutMemberCouponRequest(1L, MemberCouponStatus.USED);
		validCoupon = Coupon.builder()
			.code("VALIDCODE")
			.name("Valid Coupon")
			.discountAmount(1000)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000)
			.minPurchaseAmount(100)
			.startedAt(LocalDateTime.now().plusDays(1)) // 아직 시작되지 않은 쿠폰
			.expirationAt(LocalDateTime.now().plusDays(7))
			.build();

		memberCoupon = MemberCoupon.builder()
			.coupon(validCoupon)
			.memberId(1L)
			.status(MemberCouponStatus.UNUSED)
			.build();

		given(memberCouponRepository.findById(1L)).willReturn(Optional.of(memberCoupon));

		assertThrows(BadRequestException.class, () -> {
			memberCouponService.updateMemberCouponStatus(1L, request);
		});
	}

	@Test
	@DisplayName("유효한 회원 쿠폰 조회 테스트 - 실패 (유효한 쿠폰 없음)")
	void getMemberCouponAllByValidTestFailureNoValidCoupons() {
		given(memberCouponRepository.findValidCouponsByStatus(any(Long.class), any(String.class),
			any(MemberCouponStatus.class), any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(List.of());
		given(memberCouponRepository.findBookCouponsByMemberId(any(Long.class), any(Boolean.class))).willReturn(
			List.of());
		given(memberCouponRepository.findCategoryCouponsByMemberId(any(Long.class), any(Boolean.class))).willReturn(
			List.of());

		GetMemberCouponByTypeResponse response = memberCouponService.getMemberCouponAllByValid(1L, true);

		assertThat(response.generalCouponList()).isEmpty();
		assertThat(response.bookCouponList()).isEmpty();
		assertThat(response.categoryCouponList()).isEmpty();
	}
}
