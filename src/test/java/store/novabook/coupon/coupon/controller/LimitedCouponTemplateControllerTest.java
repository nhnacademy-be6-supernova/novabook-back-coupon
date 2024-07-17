package store.novabook.coupon.coupon.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

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
import store.novabook.coupon.coupon.dto.request.CreateLimitedCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetLimitedCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.service.LimitedCouponTemplateService;

@WebMvcTest(LimitedCouponTemplateController.class)
@Import({LimitedCouponTemplateController.class, ResponseAdvice.class})
@ContextConfiguration
@WithMockUser
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
class LimitedCouponTemplateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LimitedCouponTemplateService limitedCouponTemplateService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("모든 유효한 선착순 쿠폰 템플릿 조회")
	void testGetLimitedCouponTemplateAll() throws Exception {
		Pageable pageable = PageRequest.of(0, 10);
		GetLimitedCouponTemplateResponse couponResponse = GetLimitedCouponTemplateResponse.builder()
			.quantity(100L)
			.id(1L)
			.type(CouponType.LIMITED)
			.name("Test Coupon")
			.discountAmount(1000L)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now())
			.expirationAt(LocalDateTime.now().plusDays(10))
			.usePeriod(30)
			.build();
		Page<GetLimitedCouponTemplateResponse> page = new PageImpl<>(List.of(couponResponse), pageable, 1);

		when(limitedCouponTemplateService.findAllWithValid(any(Pageable.class))).thenReturn(page);

		mockMvc.perform(
				get("/api/v1/coupon/templates/limited").param("isValid", "true").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data[0].id").value(couponResponse.id()))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true));
	}

	@Test
	@DisplayName("새로운 선착순 쿠폰 템플릿 생성")
	void testCreateLimitedCouponTemplate() throws Exception {
		CreateLimitedCouponTemplateRequest request = new CreateLimitedCouponTemplateRequest(100L, "Test Coupon", 10L,
			DiscountType.PERCENT, 5000L, 10000L, LocalDateTime.now(), LocalDateTime.now().plusDays(10), 30);
		CreateCouponTemplateResponse response = CreateCouponTemplateResponse.builder().id(1L).build();

		when(limitedCouponTemplateService.create(any(CreateLimitedCouponTemplateRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/coupon/templates/limited").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.id").value(response.id()))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true));
	}
}
