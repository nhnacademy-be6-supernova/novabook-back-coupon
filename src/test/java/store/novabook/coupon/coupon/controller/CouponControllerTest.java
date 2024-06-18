package store.novabook.coupon.coupon.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.coupon.coupon.domain.DiscountType;
import store.novabook.coupon.coupon.dto.request.CreateBookCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.service.CouponCommandService;

@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CouponCommandService couponCommandService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("일반 쿠폰 생성 성공")
	public void saveGeneralCoupon_ShouldReturnCreated() throws Exception {
		CreateCouponRequest request = new CreateCouponRequest(
			"일반 쿠폰",
			1000,
			DiscountType.AMOUNT,
			5000,
			10000,
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(10)
		);

		CreateCouponResponse response = CreateCouponResponse.builder()
			.code("G123456789012345")
			.build();

		given(couponCommandService.saveGeneralCoupon(any(CreateCouponRequest.class))).willReturn(response);

		mockMvc.perform(post("/coupons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.code").value("G123456789012345"))
			.andDo(print());
	}

	@Test
	@DisplayName("일반 쿠폰 생성 유효성 검사 실패")
	public void saveGeneralCoupon_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
		CreateCouponRequest request = new CreateCouponRequest(
			"", // Invalid name
			1000,
			DiscountType.AMOUNT,
			5000,
			10000,
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(10)
		);

		mockMvc.perform(post("/coupons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value("INVALID_REQUEST_ARGUMENT"))
			.andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.result.name").value("이름은 필수 입력 항목입니다."))
			.andDo(print());

		// Verify that the service method was not called
		verify(couponCommandService, never()).saveGeneralCoupon(any(CreateCouponRequest.class));
	}

	@Test
	@DisplayName("책 쿠폰 생성 성공")
	public void saveBookCoupon_ShouldReturnCreated() throws Exception {
		CreateBookCouponRequest request = new CreateBookCouponRequest(
			1L,
			"책 쿠폰",
			1500,
			DiscountType.AMOUNT,
			7000,
			5000,
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(20)
		);

		CreateCouponResponse response = CreateCouponResponse.builder()
			.code("B123456789012345")
			.build();

		given(couponCommandService.saveBookCoupon(any(CreateBookCouponRequest.class))).willReturn(response);

		mockMvc.perform(post("/coupons/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.code").value("B123456789012345"))
			.andDo(print());
	}

	@Test
	@DisplayName("책 쿠폰 생성 유효성 검사 실패")
	public void saveBookCoupon_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
		CreateBookCouponRequest request = new CreateBookCouponRequest(
			null, // Invalid bookId
			"책 쿠폰",
			1500,
			DiscountType.AMOUNT,
			7000,
			5000,
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(20)
		);

		mockMvc.perform(post("/coupons/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value("INVALID_REQUEST_ARGUMENT"))
			.andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.result.bookId").value("책 ID는 필수 입력 항목입니다."))
			.andDo(print());

		// Verify that the service method was not called
		verify(couponCommandService, never()).saveBookCoupon(any(CreateBookCouponRequest.class));
	}

	@Test
	@DisplayName("카테고리 쿠폰 생성 성공")
	public void saveCategoryCoupon_ShouldReturnCreated() throws Exception {
		CreateCategoryCouponRequest request = new CreateCategoryCouponRequest(
			2L,
			"카테고리 쿠폰",
			2000,
			DiscountType.PERCENT,
			8000,
			4000,
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(15)
		);

		CreateCouponResponse response = CreateCouponResponse.builder()
			.code("C123456789012345")
			.build();

		given(couponCommandService.saveCategoryCoupon(any(CreateCategoryCouponRequest.class))).willReturn(response);

		mockMvc.perform(post("/coupons/category")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.code").value("C123456789012345"))
			.andDo(print());
	}

	@Test
	@DisplayName("카테고리 쿠폰 생성 유효성 검사 실패")
	public void saveCategoryCoupon_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
		CreateCategoryCouponRequest request = new CreateCategoryCouponRequest(
			null, // Invalid categoryId
			"카테고리 쿠폰",
			2000,
			DiscountType.PERCENT,
			8000,
			4000,
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(15)
		);

		mockMvc.perform(post("/coupons/category")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value("INVALID_REQUEST_ARGUMENT"))
			.andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.result.categoryId").value("카테고리 ID는 필수 입력 항목입니다."))
			.andDo(print());

		verify(couponCommandService, never()).saveCategoryCoupon(any(CreateCategoryCouponRequest.class));
	}
}
