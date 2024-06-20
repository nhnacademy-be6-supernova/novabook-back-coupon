package store.novabook.coupon.coupon.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.ForbiddenException;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.MemberCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.dto.MemberBookCouponDto;
import store.novabook.coupon.coupon.dto.MemberCategoryCouponDto;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponRequest;
import store.novabook.coupon.coupon.dto.request.PutMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponByTypeResponse;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.repository.MemberCouponRepository;
import store.novabook.coupon.coupon.repository.querydsl.BookCouponQueryRepository;
import store.novabook.coupon.coupon.repository.querydsl.CategoryCouponQueryRepository;

class MemberCouponServiceImplTest {

	@Mock
	private MemberCouponRepository memberCouponRepository;

	@Mock
	private CouponRepository couponRepository;

	@Mock
	private CategoryCouponQueryRepository categoryCouponQueryRepository;

	@Mock
	private BookCouponQueryRepository bookCouponQueryRepository;

	@InjectMocks
	private MemberCouponServiceImpl memberCouponService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("멤버 쿠폰 저장 성공")
	void saveMemberCoupon_Success() {
		Long memberId = 1L;
		String couponCode = "G123456789012345";
		CreateMemberCouponRequest request = new CreateMemberCouponRequest(couponCode);

		Coupon coupon = mock(Coupon.class);
		when(coupon.getExpirationAt()).thenReturn(LocalDateTime.now().plusDays(1));
		when(couponRepository.findById(couponCode)).thenReturn(Optional.of(coupon));

		MemberCoupon memberCoupon = MemberCoupon.of(memberId, coupon, MemberCouponStatus.UNUSED);
		when(memberCouponRepository.save(any(MemberCoupon.class))).thenReturn(memberCoupon);

		CreateMemberCouponResponse response = memberCouponService.saveMemberCoupon(memberId, request);

		assertNotNull(response);
		verify(couponRepository).findById(couponCode);
		verify(memberCouponRepository).save(any(MemberCoupon.class));
	}

	@Test
	@DisplayName("쿠폰을 찾을 수 없음")
	void saveMemberCoupon_CouponNotFound() {
		Long memberId = 1L;
		String couponCode = "G123456789012345";
		CreateMemberCouponRequest request = new CreateMemberCouponRequest(couponCode);

		when(couponRepository.findById(couponCode)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			memberCouponService.saveMemberCoupon(memberId, request);
		});

		assertEquals(ErrorCode.COUPON_NOT_FOUND, exception.getErrorCode());
		verify(couponRepository).findById(couponCode);
	}

	@Test
	@DisplayName("쿠폰이 만료됨")
	void saveMemberCoupon_CouponExpired() {
		Long memberId = 1L;
		String couponCode = "G123456789012345";
		CreateMemberCouponRequest request = new CreateMemberCouponRequest(couponCode);

		Coupon coupon = mock(Coupon.class);
		when(coupon.getExpirationAt()).thenReturn(LocalDateTime.now().minusDays(1));
		when(couponRepository.findById(couponCode)).thenReturn(Optional.of(coupon));

		BadRequestException exception = assertThrows(BadRequestException.class, () -> {
			memberCouponService.saveMemberCoupon(memberId, request);
		});

		assertEquals(ErrorCode.EXPIRED_COUPON_CODE, exception.getErrorCode());
		verify(couponRepository).findById(couponCode);
	}

	@Test
	@DisplayName("멤버 쿠폰 상태 업데이트 성공")
	void updateMemberCouponStatus_Success() {
		Long memberId = 1L;
		Long memberCouponId = 1L;
		PutMemberCouponRequest request = new PutMemberCouponRequest(memberCouponId, MemberCouponStatus.USED);

		MemberCoupon memberCoupon = mock(MemberCoupon.class);
		Coupon coupon = mock(Coupon.class);
		when(memberCoupon.getCoupon()).thenReturn(coupon);
		when(coupon.getExpirationAt()).thenReturn(LocalDateTime.now().plusDays(1));
		when(coupon.getStartedAt()).thenReturn(LocalDateTime.now().minusDays(1));
		when(memberCoupon.getStatus()).thenReturn(MemberCouponStatus.UNUSED);
		when(memberCoupon.getMemberId()).thenReturn(memberId);

		when(memberCouponRepository.findById(memberCouponId)).thenReturn(Optional.of(memberCoupon));

		memberCouponService.updateMemberCouponStatus(memberId, request);

		verify(memberCoupon).updateStatus(MemberCouponStatus.USED);
		verify(memberCouponRepository).save(memberCoupon);
	}

	@Test
	@DisplayName("멤버 쿠폰을 찾을 수 없음")
	void updateMemberCouponStatus_NotFound() {
		Long memberId = 1L;
		Long memberCouponId = 1L;
		PutMemberCouponRequest request = new PutMemberCouponRequest(memberCouponId, MemberCouponStatus.USED);

		when(memberCouponRepository.findById(memberCouponId)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			memberCouponService.updateMemberCouponStatus(memberId, request);
		});

		assertEquals(ErrorCode.COUPON_NOT_FOUND, exception.getErrorCode());
		verify(memberCouponRepository).findById(memberCouponId);
	}

	@Test
	@DisplayName("잘못된 쿠폰")
	void updateMemberCouponStatus_InvalidCoupon() {
		Long memberId = 1L;
		Long memberCouponId = 1L;
		PutMemberCouponRequest request = new PutMemberCouponRequest(memberCouponId, MemberCouponStatus.USED);

		MemberCoupon memberCoupon = mock(MemberCoupon.class);
		Coupon coupon = mock(Coupon.class);
		when(memberCoupon.getCoupon()).thenReturn(coupon);
		when(coupon.getExpirationAt()).thenReturn(LocalDateTime.now().minusDays(1));
		when(coupon.getStartedAt()).thenReturn(LocalDateTime.now().plusDays(1));
		when(memberCoupon.getStatus()).thenReturn(MemberCouponStatus.USED);
		when(memberCoupon.getMemberId()).thenReturn(memberId);

		when(memberCouponRepository.findById(memberCouponId)).thenReturn(Optional.of(memberCoupon));

		BadRequestException exception = assertThrows(BadRequestException.class, () -> {
			memberCouponService.updateMemberCouponStatus(memberId, request);
		});

		assertEquals(ErrorCode.INVALID_COUPON, exception.getErrorCode());
		verify(memberCouponRepository).findById(memberCouponId);
	}

	@Test
	@DisplayName("권한 없음")
	void updateMemberCouponStatus_Forbidden() {
		Long memberId = 1L;
		Long memberCouponId = 1L;
		Long otherMemberId = 2L;
		PutMemberCouponRequest request = new PutMemberCouponRequest(memberCouponId, MemberCouponStatus.USED);

		MemberCoupon memberCoupon = mock(MemberCoupon.class);
		Coupon coupon = mock(Coupon.class);
		when(memberCoupon.getCoupon()).thenReturn(coupon);
		when(coupon.getExpirationAt()).thenReturn(LocalDateTime.now().plusDays(1));
		when(coupon.getStartedAt()).thenReturn(LocalDateTime.now().minusDays(1));
		when(memberCoupon.getStatus()).thenReturn(MemberCouponStatus.UNUSED);
		when(memberCoupon.getMemberId()).thenReturn(otherMemberId);

		when(memberCouponRepository.findById(memberCouponId)).thenReturn(Optional.of(memberCoupon));

		ForbiddenException exception = assertThrows(ForbiddenException.class, () -> {
			memberCouponService.updateMemberCouponStatus(memberId, request);
		});

		assertEquals(ErrorCode.NOT_ENOUGH_PERMISSION, exception.getErrorCode());
		verify(memberCouponRepository).findById(memberCouponId);
	}

	@Test
	@DisplayName("상태별 모든 멤버 쿠폰 가져오기")
	void getMemberCouponAllByStatus() {
		Long memberId = 1L;
		MemberCouponStatus status = MemberCouponStatus.UNUSED;
		Pageable pageable = PageRequest.of(0, 10);

		MemberCoupon memberCoupon = mock(MemberCoupon.class);
		Coupon coupon = mock(Coupon.class);
		when(memberCoupon.getCoupon()).thenReturn(coupon);  // Ensure getCoupon() does not return null

		Page<MemberCoupon> memberCouponPage = new PageImpl<>(List.of(memberCoupon));
		when(memberCouponRepository.findAllByStatus(status, pageable)).thenReturn(memberCouponPage);

		GetMemberCouponAllResponse response = memberCouponService.getMemberCouponAllByStatus(memberId, status,
			pageable);

		assertNotNull(response);
		assertEquals(1, response.memberCouponPage().getTotalElements());
		verify(memberCouponRepository).findAllByStatus(status, pageable);
	}

	@Test
	@DisplayName("유효한 모든 멤버 쿠폰 가져오기")
	void getMemberCouponAllByValid() {
		Long memberId = 1L;
		Boolean validOnly = true;

		MemberCoupon memberCoupon = mock(MemberCoupon.class);
		when(memberCoupon.getCoupon()).thenReturn(mock(Coupon.class));
		List<MemberCoupon> generalCoupons = List.of(memberCoupon);
		when(memberCouponRepository.findValidCouponsByStatus(anyLong(), anyString(), any(), any(), any())).thenReturn(
			generalCoupons);

		MemberBookCouponDto bookCouponDto = mock(MemberBookCouponDto.class);
		List<MemberBookCouponDto> bookCoupons = List.of(bookCouponDto);
		when(bookCouponQueryRepository.findBookCouponsByMemberId(anyLong(), anyBoolean())).thenReturn(bookCoupons);

		MemberCategoryCouponDto categoryCouponDto = mock(MemberCategoryCouponDto.class);
		List<MemberCategoryCouponDto> categoryCoupons = List.of(categoryCouponDto);
		when(categoryCouponQueryRepository.findCategoryCouponsByMemberId(anyLong(), anyBoolean())).thenReturn(
			categoryCoupons);

		GetMemberCouponByTypeResponse response = memberCouponService.getMemberCouponAllByValid(memberId, validOnly);

		assertNotNull(response);
		assertEquals(1, response.generalCouponList().size());
		assertEquals(1, response.bookCouponList().size());
		assertEquals(1, response.categoryCouponList().size());

		verify(memberCouponRepository).findValidCouponsByStatus(anyLong(), anyString(), any(), any(), any());
		verify(bookCouponQueryRepository).findBookCouponsByMemberId(anyLong(), anyBoolean());
		verify(categoryCouponQueryRepository).findCategoryCouponsByMemberId(anyLong(), anyBoolean());
	}
}
