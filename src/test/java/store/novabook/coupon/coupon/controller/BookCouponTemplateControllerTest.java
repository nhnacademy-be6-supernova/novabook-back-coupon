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
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.coupon.common.handler.ResponseAdvice;
import store.novabook.coupon.coupon.dto.request.CreateBookCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetBookCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetBookCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.service.BookCouponTemplateService;

@WithMockUser
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(BookCouponTemplateController.class)
@Import({BookCouponTemplateController.class, ResponseAdvice.class})
class BookCouponTemplateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookCouponTemplateService bookCouponTemplateService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("모든 도서 쿠폰 템플릿 조회")
	void testGetBookCouponTemplateAll() throws Exception {
		Pageable pageable = PageRequest.of(0, 10);
		GetBookCouponTemplateResponse response = GetBookCouponTemplateResponse.builder()
			.bookId(1L)
			.id(1L)
			.type(CouponType.BOOK)
			.name("Test Coupon")
			.discountAmount(1000L)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now())
			.expirationAt(LocalDateTime.now().plusDays(10))
			.usePeriod(30)
			.build();
		Page<GetBookCouponTemplateResponse> page = new PageImpl<>(Collections.singletonList(response), pageable, 1);

		when(bookCouponTemplateService.findAll(any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/api/v1/coupon/templates/book").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data[0].id").value(response.id()))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true));
	}

	@Test
	@DisplayName("새로운 도서 쿠폰 템플릿 생성")
	void testCreateBookCouponTemplate() throws Exception {
		CreateBookCouponTemplateRequest request = new CreateBookCouponTemplateRequest(1L, "Test Coupon", 10L,
			DiscountType.PERCENT, 5000L, 10000L, LocalDateTime.now(), LocalDateTime.now().plusDays(10), 30);
		CreateCouponTemplateResponse response = CreateCouponTemplateResponse.builder().id(1L).build();

		when(bookCouponTemplateService.create(any())).thenReturn(response);

		mockMvc.perform(post("/api/v1/coupon/templates/book").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.id").value(response.id()))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true));
	}

	@Test
	@DisplayName("특정 도서의 쿠폰 템플릿 조회")
	void testGetCouponTemplateByBookId() throws Exception {
		GetBookCouponTemplateResponse couponResponse = GetBookCouponTemplateResponse.builder()
			.bookId(1L)
			.id(1L)
			.type(CouponType.BOOK)
			.name("Test Coupon")
			.discountAmount(1000L)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now())
			.expirationAt(LocalDateTime.now().plusDays(10))
			.usePeriod(30)
			.build();
		GetBookCouponTemplateAllResponse response = GetBookCouponTemplateAllResponse.builder()
			.responseList(Collections.singletonList(couponResponse))
			.build();

		when(bookCouponTemplateService.findAllByBookId(anyLong(), anyBoolean())).thenReturn(response);

		mockMvc.perform(get("/api/v1/coupon/templates/book/{bookId}", 1L).contentType(MediaType.APPLICATION_JSON)
				.param("isValid", "true"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.responseList").isArray())
			.andExpect(jsonPath("$.body.responseList[0].id").value(response.responseList().get(0).id()))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true));
	}
}