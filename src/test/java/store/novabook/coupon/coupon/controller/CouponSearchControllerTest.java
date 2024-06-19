package store.novabook.coupon.coupon.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import store.novabook.coupon.CouponApplication;
import store.novabook.coupon.coupon.domain.DiscountType;
import store.novabook.coupon.coupon.dto.response.GetCouponBookAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.service.CouponService;

@SpringBootTest(classes = CouponApplication.class)
@AutoConfigureMockMvc
public class CouponSearchControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CouponService couponService;

	private GetCouponBookAllResponse getCouponBookAllResponse;
	private GetCouponCategoryAllResponse getCouponCategoryAllResponse;

	@BeforeEach
	void setUp() {
		GetCouponResponse bookCouponResponse = GetCouponResponse.builder()
			.code("B123456789012345")
			.name("Book Discount")
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(0L)
			.startedAt(LocalDateTime.parse("2023-01-01T00:00:00"))
			.expirationAt(LocalDateTime.parse("2023-12-31T23:59:59"))
			.build();

		getCouponBookAllResponse = GetCouponBookAllResponse.builder()
			.couponResponseList(List.of(bookCouponResponse))
			.build();

		GetCouponResponse categoryCouponResponse = GetCouponResponse.builder()
			.code("C123456789012345")
			.name("Category Discount")
			.discountAmount(1500L)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(10000L)
			.minPurchaseAmount(1000L)
			.startedAt(LocalDateTime.parse("2023-01-01T00:00:00"))
			.expirationAt(LocalDateTime.parse("2023-12-31T23:59:59"))
			.build();

		getCouponCategoryAllResponse = GetCouponCategoryAllResponse.builder()
			.couponResponseList(List.of(categoryCouponResponse))
			.build();
	}

	@Test
	@DisplayName("책 쿠폰 조회")
	void getCouponBook_ReturnsCoupons() throws Exception {
		given(couponService.getCouponBook(anyLong())).willReturn(getCouponBookAllResponse);

		mockMvc.perform(get("/coupons/book/{bookId}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.couponResponseList[0].code").value("B123456789012345"))
			.andExpect(jsonPath("$.couponResponseList[0].name").value("Book Discount"))
			.andExpect(jsonPath("$.couponResponseList[0].discountAmount").value(1000))
			.andExpect(jsonPath("$.couponResponseList[0].discountType").value("AMOUNT"))
			.andExpect(jsonPath("$.couponResponseList[0].maxDiscountAmount").value(5000))
			.andExpect(jsonPath("$.couponResponseList[0].minPurchaseAmount").value(0))
			.andExpect(jsonPath("$.couponResponseList[0].startedAt").value("2023-01-01T00:00:00"))
			.andExpect(jsonPath("$.couponResponseList[0].expirationAt").value("2023-12-31T23:59:59"));
	}

	@Test
	@DisplayName("카테고리 쿠폰 조회")
	void getCouponCategory_ReturnsCoupons() throws Exception {
		given(couponService.getCouponCategory(anyLong())).willReturn(getCouponCategoryAllResponse);

		mockMvc.perform(get("/coupons/category/{categoryId}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.couponResponseList[0].code").value("C123456789012345"))
			.andExpect(jsonPath("$.couponResponseList[0].name").value("Category Discount"))
			.andExpect(jsonPath("$.couponResponseList[0].discountAmount").value(1500))
			.andExpect(jsonPath("$.couponResponseList[0].discountType").value("PERCENT"))
			.andExpect(jsonPath("$.couponResponseList[0].maxDiscountAmount").value(10000))
			.andExpect(jsonPath("$.couponResponseList[0].minPurchaseAmount").value(1000))
			.andExpect(jsonPath("$.couponResponseList[0].startedAt").value("2023-01-01T00:00:00"))
			.andExpect(jsonPath("$.couponResponseList[0].expirationAt").value("2023-12-31T23:59:59"));
	}
}
