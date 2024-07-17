package store.novabook.coupon.coupon.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;

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
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.service.CategoryCouponTemplateService;

@WithMockUser
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(CategoryCouponTemplateController.class)
@Import({CategoryCouponTemplateController.class, ResponseAdvice.class})
@ContextConfiguration
class CategoryCouponTemplateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryCouponTemplateService categoryCouponTemplateService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("모든 카테고리 쿠폰 템플릿 조회")
	void testGetCategoryCouponTemplateAll() throws Exception {
		Pageable pageable = PageRequest.of(0, 10);
		GetCategoryCouponTemplateResponse response = GetCategoryCouponTemplateResponse.builder()
			.categoryId(1L)
			.id(1L)
			.type(CouponType.CATEGORY)
			.name("Test Coupon")
			.discountAmount(1000L)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now())
			.expirationAt(LocalDateTime.now().plusDays(10))
			.usePeriod(30)
			.build();
		Page<GetCategoryCouponTemplateResponse> page = new PageImpl<>(Collections.singletonList(response), pageable, 1);

		when(categoryCouponTemplateService.findAllWithValid(any(Pageable.class))).thenReturn(page);

		mockMvc.perform(
				get("/api/v1/coupon/templates/category").param("isValid", "true").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data[0].id").value(response.id()))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true));
	}

	@Test
	@DisplayName("새로운 카테고리 쿠폰 템플릿 생성")
	void testCreateCategoryCouponTemplate() throws Exception {
		CreateCategoryCouponTemplateRequest request = new CreateCategoryCouponTemplateRequest(1L, "Test Coupon", 10L,
			DiscountType.PERCENT, 5000L, 10000L, LocalDateTime.now(), LocalDateTime.now().plusDays(10), 30);
		CreateCouponTemplateResponse response = CreateCouponTemplateResponse.builder().id(1L).build();

		when(categoryCouponTemplateService.create(any(CreateCategoryCouponTemplateRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/coupon/templates/category").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.id").value(response.id()))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true));
	}

	@Test
	@DisplayName("여러 카테고리 ID로 쿠폰 템플릿 조회")
	void testGetCategoryCouponTemplateAllByCategoryIdAll() throws Exception {
		GetCategoryCouponTemplateResponse couponResponse = GetCategoryCouponTemplateResponse.builder()
			.categoryId(1L)
			.id(1L)
			.type(CouponType.CATEGORY)
			.name("Test Coupon")
			.discountAmount(1000L)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now())
			.expirationAt(LocalDateTime.now().plusDays(10))
			.usePeriod(30)
			.build();
		GetCategoryCouponTemplateAllResponse response = GetCategoryCouponTemplateAllResponse.builder()
			.responseList(Collections.singletonList(couponResponse))
			.build();

		when(categoryCouponTemplateService.findAllByCategoryId(anyList(), anyBoolean())).thenReturn(response);

		mockMvc.perform(get("/api/v1/coupon/templates/category/categories").param("categoryIdList", "1")
				.param("isValid", "true")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.responseList").isArray())
			.andExpect(jsonPath("$.body.responseList[0].id").value(response.responseList().get(0).id()))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true));
	}
}
