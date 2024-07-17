package store.novabook.coupon.coupon.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import store.novabook.coupon.coupon.dto.request.CreateCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.service.CouponTemplateService;

@ExtendWith(MockitoExtension.class)
class CouponTemplateControllerTest {

	@Mock
	private CouponTemplateService couponTemplateService;

	@InjectMocks
	private CouponTemplateController couponTemplateController;

	@Test
	@DisplayName("쿠폰 템플릿 타입으로 조회 - 유효성 여부 포함")
	void getCouponTemplateAllByType_유효성_포함() {
		CouponType type = CouponType.GENERAL;
		boolean isValid = true;
		Pageable pageable = Pageable.unpaged();
		Page<GetCouponTemplateResponse> expectedResponsePage = new PageImpl<>(Collections.emptyList());

		when(couponTemplateService.findByTypeAndValid(type, isValid, pageable))
			.thenReturn(expectedResponsePage);

		ResponseEntity<Page<GetCouponTemplateResponse>> responseEntity =
			couponTemplateController.getCouponTemplateAllByType(type, isValid, pageable);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(expectedResponsePage, responseEntity.getBody());

		verify(couponTemplateService, times(1)).findByTypeAndValid(type, isValid, pageable);
		verifyNoMoreInteractions(couponTemplateService);
	}

	@Test
	@DisplayName("쿠폰 템플릿 전체 조회 - 페이지 정보 사용")
	void getCouponTemplateAll_페이지_사용() {
		Pageable pageable = Pageable.unpaged();
		Page<GetCouponTemplateResponse> expectedResponsePage = new PageImpl<>(Collections.emptyList());

		when(couponTemplateService.findAll(pageable))
			.thenReturn(expectedResponsePage);

		ResponseEntity<Page<GetCouponTemplateResponse>> responseEntity =
			couponTemplateController.getCouponTemplateAll(pageable);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(expectedResponsePage, responseEntity.getBody());

		verify(couponTemplateService, times(1)).findAll(pageable);
		verifyNoMoreInteractions(couponTemplateService);
	}

	@Test
	@DisplayName("쿠폰 템플릿 생성 - 유효한 요청")
	void createCouponTemplate_유효한_요청() {
		CreateCouponTemplateRequest request = new CreateCouponTemplateRequest(
			"Test Coupon", CouponType.GENERAL, 1000L, null, 5000L, 10000L,
			LocalDateTime.now(), LocalDateTime.now().plusDays(30), 30
		);
		CreateCouponTemplateResponse expectedResponse = new CreateCouponTemplateResponse(1L);

		when(couponTemplateService.create(ArgumentMatchers.any(CreateCouponTemplateRequest.class)))
			.thenReturn(expectedResponse);

		ResponseEntity<CreateCouponTemplateResponse> responseEntity =
			couponTemplateController.createCouponTemplate(request);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(expectedResponse, responseEntity.getBody());

		verify(couponTemplateService, times(1)).create(request);
		verifyNoMoreInteractions(couponTemplateService);
	}
}
