package store.novabook.coupon.coupon.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.ForbiddenException;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.common.message.MemberRegistrationMessage;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.DiscountType;
import store.novabook.coupon.coupon.domain.MemberCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponAllRequest;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponRequest;
import store.novabook.coupon.coupon.dto.request.PutMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponBookResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponByTypeResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponCategoryResponse;
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

	@Mock
	private RabbitTemplate rabbitTemplate;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("쿠폰 저장 - 성공")
	void saveMemberCoupon_성공() {
		Long memberId = 1L;
		CreateMemberCouponRequest request = CreateMemberCouponRequest.builder()
			.couponCode("coupon123")
			.expirationAt(LocalDateTime.now().plusDays(1))
			.build();

		Coupon coupon = Coupon.builder()
			.expirationAt(LocalDateTime.now().plusDays(5))
			.startedAt(LocalDateTime.now().minusDays(1))
			.build();

		when(couponRepository.findById(request.couponCode())).thenReturn(Optional.of(coupon));
		when(memberCouponRepository.save(any(MemberCoupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

		CreateMemberCouponResponse response = memberCouponService.saveMemberCoupon(memberId, request);

		assertNotNull(response);
		verify(couponRepository).findById(request.couponCode());
		verify(memberCouponRepository).save(any(MemberCoupon.class));
	}

	@Test
	@DisplayName("쿠폰 저장 - 쿠폰을 찾을 수 없음")
	void saveMemberCoupon_쿠폰을_찾을_수_없음() {
		Long memberId = 1L;
		CreateMemberCouponRequest request = CreateMemberCouponRequest.builder()
			.couponCode("coupon123")
			.expirationAt(LocalDateTime.now().plusDays(1))
			.build();

		when(couponRepository.findById(request.couponCode())).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class,
			() -> memberCouponService.saveMemberCoupon(memberId, request));

		assertEquals(ErrorCode.COUPON_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	@DisplayName("쿠폰 저장 - 만료된 쿠폰")
	void saveMemberCoupon_만료된_쿠폰() {
		Long memberId = 1L;
		CreateMemberCouponRequest request = CreateMemberCouponRequest.builder()
			.couponCode("coupon123")
			.expirationAt(LocalDateTime.now().plusDays(1))
			.build();

		Coupon coupon = Coupon.builder().expirationAt(LocalDateTime.now().minusDays(1)).build();

		when(couponRepository.findById(request.couponCode())).thenReturn(Optional.of(coupon));

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> memberCouponService.saveMemberCoupon(memberId, request));

		assertEquals(ErrorCode.EXPIRED_COUPON_CODE, exception.getErrorCode());
	}

	@Test
	@DisplayName("쿠폰 저장 - 사용 불가한 쿠폰")
	void saveMemberCoupon_사용_불가한_쿠폰() {
		Long memberId = 1L;
		CreateMemberCouponRequest request = CreateMemberCouponRequest.builder()
			.couponCode("coupon123")
			.expirationAt(LocalDateTime.now().plusDays(1))
			.build();

		Coupon coupon = Coupon.builder()
			.startedAt(LocalDateTime.now().plusDays(1))
			.expirationAt(LocalDateTime.now().plusDays(10))  // expirationAt 값을 설정
			.build();

		when(couponRepository.findById(request.couponCode())).thenReturn(Optional.of(coupon));

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> memberCouponService.saveMemberCoupon(memberId, request));

		assertEquals(ErrorCode.EXPIRED_COUPON_CODE, exception.getErrorCode());
	}

	@Test
	@DisplayName("회원 웰컴 쿠폰 저장 - 웰컴 쿠폰을 찾을 수 없음")
	void saveMemberWelcomeCoupon_웰컴_쿠폰을_찾을_수_없음() {
		Long memberId = 1L;
		MemberRegistrationMessage message = new MemberRegistrationMessage(memberId);

		when(couponRepository.findTopByCodeStartsWithOrderByCreatedAtDesc("WELCOME")).thenReturn(Optional.empty());

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> memberCouponService.saveMemberWelcomeCoupon(message));

		assertEquals(ErrorCode.WELCOME_COUPON_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	@DisplayName("모든 쿠폰 조회 - 성공")
	void getMemberCouponAllByValid_성공() {
		Long memberId = 1L;
		Boolean validOnly = true;

		List<GetMemberCouponResponse> generalCoupons = Arrays.asList(GetMemberCouponResponse.builder().build());
		List<GetMemberCouponBookResponse> bookCoupons = Arrays.asList(GetMemberCouponBookResponse.builder().build());
		List<GetMemberCouponCategoryResponse> categoryCoupons = Arrays.asList(
			GetMemberCouponCategoryResponse.builder().build());

		when(memberCouponRepository.findGeneralCouponsByMemberId(memberId, validOnly)).thenReturn(generalCoupons);
		when(memberCouponRepository.findBookCouponsByMemberId(memberId, validOnly)).thenReturn(bookCoupons);
		when(memberCouponRepository.findCategoryCouponsByMemberId(memberId, validOnly)).thenReturn(categoryCoupons);

		GetMemberCouponByTypeResponse response = memberCouponService.getMemberCouponAllByValid(memberId, validOnly);

		assertNotNull(response);
		assertEquals(1, response.generalCouponList().size());
		assertEquals(1, response.bookCouponList().size());
		assertEquals(1, response.categoryCouponList().size());
	}

	@Test
	@DisplayName("쿠폰 상태별 조회 - 성공")
	void getMemberCouponAllByStatus_성공() {
		Long memberId = 1L;
		MemberCouponStatus status = MemberCouponStatus.UNUSED;
		Pageable pageable = PageRequest.of(0, 10);

		Coupon coupon = Coupon.builder()
			.code("coupon123")
			.name("Test Coupon")
			.discountAmount(1000L)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(1000L)
			.expirationAt(LocalDateTime.now().plusDays(10))
			.startedAt(LocalDateTime.now().minusDays(1))
			.build();

		MemberCoupon memberCoupon = MemberCoupon.builder()
			.coupon(coupon)
			.status(status)
			.memberId(memberId)
			.expirationAt(LocalDateTime.now().plusDays(1))
			.build();

		List<MemberCoupon> coupons = Arrays.asList(memberCoupon);
		Page<MemberCoupon> page = new PageImpl<>(coupons);

		when(memberCouponRepository.findAllByStatus(status, pageable)).thenReturn(page);

		Page<GetMemberCouponResponse> response = memberCouponService.getMemberCouponAllByStatus(memberId, status,
			pageable);

		assertNotNull(response);
		assertEquals(1, response.getContent().size());
	}

	@Test
	@DisplayName("회원 쿠폰 상태 업데이트 - 성공")
	void updateMemberCouponStatus_성공() {
		Long memberId = 1L;
		Long memberCouponId = 1L;  // memberCouponId를 Long으로 변경
		PutMemberCouponRequest request = new PutMemberCouponRequest(MemberCouponStatus.USED);

		Coupon coupon = Coupon.builder()
			.code("coupon123")
			.name("Test Coupon")
			.discountAmount(1000L)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(1000L)
			.expirationAt(LocalDateTime.now().plusDays(10))
			.startedAt(LocalDateTime.now().minusDays(1))
			.build();

		MemberCoupon memberCoupon = MemberCoupon.builder()
			.coupon(coupon)
			.memberId(memberId)
			.status(MemberCouponStatus.UNUSED)
			.expirationAt(LocalDateTime.now().plusDays(1))
			.build();

		when(memberCouponRepository.findById(memberCouponId)).thenReturn(Optional.of(memberCoupon));

		memberCouponService.updateMemberCouponStatus(memberId, memberCouponId.toString(), request);

		verify(memberCouponRepository).findById(memberCouponId);
		assertEquals(MemberCouponStatus.USED, memberCoupon.getStatus());
	}

	@Test
	@DisplayName("회원 쿠폰 상태 업데이트 - 쿠폰을 찾을 수 없음")
	void updateMemberCouponStatus_쿠폰을_찾을_수_없음() {
		Long memberId = 1L;
		String memberCouponId = "memberCoupon123";
		PutMemberCouponRequest request = new PutMemberCouponRequest(MemberCouponStatus.USED);

		when(memberCouponRepository.findById(memberId)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class,
			() -> memberCouponService.updateMemberCouponStatus(memberId, memberCouponId, request));

		assertEquals(ErrorCode.COUPON_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	@DisplayName("회원 쿠폰 상태 업데이트 - 권한 없음")
	void updateMemberCouponStatus_권한_없음() {
		Long memberId = 1L;
		String memberCouponId = "memberCoupon123";
		PutMemberCouponRequest request = new PutMemberCouponRequest(MemberCouponStatus.USED);

		MemberCoupon memberCoupon = MemberCoupon.builder().memberId(2L) // 다른 회원 ID로 설정
			.status(MemberCouponStatus.UNUSED).expirationAt(LocalDateTime.now().plusDays(1)).build();

		when(memberCouponRepository.findById(memberId)).thenReturn(Optional.of(memberCoupon));

		ForbiddenException exception = assertThrows(ForbiddenException.class,
			() -> memberCouponService.updateMemberCouponStatus(memberId, memberCouponId, request));

		assertEquals(ErrorCode.NOT_ENOUGH_PERMISSION, exception.getErrorCode());
	}

	@Test
	@DisplayName("회원 쿠폰 상태 업데이트 - 만료된 쿠폰")
	void updateMemberCouponStatus_만료된_쿠폰() {
		Long memberId = 1L;
		String memberCouponId = "memberCoupon123";
		PutMemberCouponRequest request = new PutMemberCouponRequest(MemberCouponStatus.USED);

		MemberCoupon memberCoupon = MemberCoupon.builder()
			.memberId(memberId)
			.status(MemberCouponStatus.UNUSED)
			.expirationAt(LocalDateTime.now().minusDays(1)) // 만료된 쿠폰
			.build();

		when(memberCouponRepository.findById(memberId)).thenReturn(Optional.of(memberCoupon));

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> memberCouponService.updateMemberCouponStatus(memberId, memberCouponId, request));

		assertEquals(ErrorCode.INVALID_COUPON, exception.getErrorCode());
	}

	@Test
	@DisplayName("모든 회원 쿠폰 저장 - 성공")
	void saveMemberCouponAll_성공() {
		CreateMemberCouponAllRequest request = CreateMemberCouponAllRequest.builder()
			.couponCode("coupon123")
			.memberIdList(Arrays.asList(1L, 2L))
			.expirationAt(LocalDateTime.now().plusDays(1))
			.build();

		Coupon coupon = Coupon.builder()
			.expirationAt(LocalDateTime.now().plusDays(5))
			.startedAt(LocalDateTime.now().minusDays(1))
			.build();

		when(couponRepository.findById(request.couponCode())).thenReturn(Optional.of(coupon));
		when(memberCouponRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

		CreateMemberCouponAllResponse response = memberCouponService.saveMemberCouponAll(request);

		assertNotNull(response);
		assertEquals(2, response.memberCouponResponseList().size());
		verify(couponRepository).findById(request.couponCode());
		verify(memberCouponRepository).saveAll(anyList());
	}

	@Test
	@DisplayName("모든 회원 쿠폰 저장 - 쿠폰을 찾을 수 없음")
	void saveMemberCouponAll_쿠폰을_찾을_수_없음() {
		CreateMemberCouponAllRequest request = CreateMemberCouponAllRequest.builder()
			.couponCode("coupon123")
			.memberIdList(Arrays.asList(1L, 2L))
			.expirationAt(LocalDateTime.now().plusDays(1))
			.build();

		when(couponRepository.findById(request.couponCode())).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class,
			() -> memberCouponService.saveMemberCouponAll(request));

		assertEquals(ErrorCode.COUPON_NOT_FOUND, exception.getErrorCode());
	}
}
