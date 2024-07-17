package store.novabook.coupon.coupon.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.coupon.common.handler.ResponseAdvice;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.service.CouponService;

@WebMvcTest(CouponController.class)
@Import({CouponController.class, ResponseAdvice.class})
@ContextConfiguration
@WithMockUser
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
class CouponControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CouponService couponService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("유효한 모든 쿠폰 조회")
	void testGetSufficientCouponAll() throws Exception {
		GetCouponAllRequest request = new GetCouponAllRequest(List.of(1L, 2L), Set.of(1L), Set.of(1L));
		GetCouponResponse couponResponse = GetCouponResponse.builder()
			.id(1L)
			.type(CouponType.GENERAL)
			.status(CouponStatus.UNUSED)
			.name("Test Coupon")
			.discountAmount(1000L)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.createdAt(LocalDateTime.now())
			.expirationAt(LocalDateTime.now().plusDays(10))
			.build();
		GetCouponAllResponse response = GetCouponAllResponse.builder()
			.couponResponseList(List.of(couponResponse))
			.build();

		when(couponService.findSufficientCouponAllById(any(GetCouponAllRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/coupon/coupons/sufficient").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.couponResponseList").isArray())
			.andExpect(jsonPath("$.body.couponResponseList[0].id").value(couponResponse.id()))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true));
	}

	@Test
	@DisplayName("새로운 쿠폰 생성")
	void testCreateCoupon() throws Exception {
		CreateCouponRequest request = new CreateCouponRequest(List.of(1L, 2L), 1L);
		CreateCouponResponse response = CreateCouponResponse.builder().id(1L).build();

		when(couponService.create(any(CreateCouponRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/coupon/coupons").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.id").value(response.id()))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true));
	}

	@Test
	@DisplayName("쿠폰 사용처리")
	void testUseCoupon() throws Exception {
		mockMvc.perform(put("/api/v1/coupon/coupons/{couponId}", 1L).with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true));
	}

	@Test
	@DisplayName("쿠폰 상태에 따른 쿠폰 조회")
	void testGetCouponAllWithPageable() throws Exception {
		Pageable pageable = PageRequest.of(0, 10);
		GetCouponResponse couponResponse = GetCouponResponse.builder()
			.id(1L)
			.type(CouponType.GENERAL)
			.status(CouponStatus.UNUSED)
			.name("Test Coupon")
			.discountAmount(1000L)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.createdAt(LocalDateTime.now())
			.expirationAt(LocalDateTime.now().plusDays(10))
			.build();
		Page<GetCouponResponse> page = new PageImpl<>(List.of(couponResponse), pageable, 1);

		when(couponService.findAllByIdAndStatus(anyList(), any(CouponStatus.class), any(Pageable.class))).thenReturn(
			page);

		mockMvc.perform(get("/api/v1/coupon/coupons").param("couponIdList", "1")
				.param("status", "UNUSED")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data[0].id").value(couponResponse.id()))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true));
	}

	@Test
	@DisplayName("유효한 쿠폰 조회")
	void testGetCouponAllWithPageableIsValid() throws Exception {
		GetCouponResponse couponResponse = GetCouponResponse.builder()
			.id(1L)
			.type(CouponType.GENERAL)
			.status(CouponStatus.UNUSED)
			.name("Test Coupon")
			.discountAmount(1000L)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.createdAt(LocalDateTime.now())
			.expirationAt(LocalDateTime.now().plusDays(10))
			.build();
		GetCouponAllResponse response = GetCouponAllResponse.builder()
			.couponResponseList(List.of(couponResponse))
			.build();

		when(couponService.findAllValidById(anyList())).thenReturn(response);

		mockMvc.perform(
				get("/api/v1/coupon/coupons/is-valid").param("couponIdList", "1").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.couponResponseList").isArray())
			.andExpect(jsonPath("$.body.couponResponseList[0].id").value(couponResponse.id()))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true));
	}
}
