package store.novabook.coupon.coupon.controller;

import static org.hamcrest.collection.IsCollectionWithSize.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.DiscountType;
import store.novabook.coupon.coupon.dto.request.CreateCouponBookRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponCategoryRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.UpdateCouponExpirationRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.service.CommonCouponService;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("insert-coupons.sql")
public class CommonCouponControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CommonCouponService commonCouponService;

	@MockBean
	private CouponRepository couponRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("일반 쿠폰 생성 성공")
	public void saveGeneralCoupon_ShouldReturnCreated() throws Exception {
		CreateCouponRequest request = new CreateCouponRequest("일반 쿠폰", 1000, DiscountType.AMOUNT, 5000, 10000,
			LocalDateTime.now(), LocalDateTime.now().plusDays(10));

		CreateCouponResponse response = CreateCouponResponse.builder().code("G123456789012345").build();

		given(commonCouponService.saveGeneralCoupon(any(CreateCouponRequest.class))).willReturn(response);

		mockMvc.perform(
				post("/coupons/general").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.code").value("G123456789012345"))
			.andDo(print());
	}

	@Test
	@DisplayName("일반 쿠폰 생성 유효성 검사 실패")
	public void saveGeneralCoupon_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
		CreateCouponRequest request = new CreateCouponRequest("", // Invalid name
			1000, DiscountType.AMOUNT, 5000, 10000, LocalDateTime.now(), LocalDateTime.now().plusDays(10));

		mockMvc.perform(
				post("/coupons/general").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value("INVALID_REQUEST_ARGUMENT"))
			.andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.result.name").value("이름은 필수 입력 항목입니다."))
			.andDo(print());

		verify(commonCouponService, never()).saveGeneralCoupon(any(CreateCouponRequest.class));
	}

	@Test
	@DisplayName("책 쿠폰 생성 성공")
	public void saveBookCoupon_ShouldReturnCreated() throws Exception {
		CreateCouponBookRequest request = new CreateCouponBookRequest(1L, "책 쿠폰", 1500, DiscountType.AMOUNT, 7000, 5000,
			LocalDateTime.now(), LocalDateTime.now().plusDays(20));

		CreateCouponResponse response = CreateCouponResponse.builder().code("B123456789012345").build();

		given(commonCouponService.saveBookCoupon(any(CreateCouponBookRequest.class))).willReturn(response);

		mockMvc.perform(post("/coupons/book").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.code").value("B123456789012345"))
			.andDo(print());
	}

	@Test
	@DisplayName("책 쿠폰 생성 유효성 검사 실패")
	public void saveBookCoupon_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
		CreateCouponBookRequest request = new CreateCouponBookRequest(null, // Invalid bookId
			"책 쿠폰", 1500, DiscountType.AMOUNT, 7000, 5000, LocalDateTime.now(), LocalDateTime.now().plusDays(20));

		mockMvc.perform(post("/coupons/book").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value("INVALID_REQUEST_ARGUMENT"))
			.andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.result.bookId").value("책 ID는 필수 입력 항목입니다."))
			.andDo(print());

		verify(commonCouponService, never()).saveBookCoupon(any(CreateCouponBookRequest.class));
	}

	@Test
	@DisplayName("카테고리 쿠폰 생성 성공")
	public void saveCategoryCoupon_ShouldReturnCreated() throws Exception {
		CreateCouponCategoryRequest request = new CreateCouponCategoryRequest(2L, "카테고리 쿠폰", 2000, DiscountType.PERCENT,
			8000, 4000, LocalDateTime.now(), LocalDateTime.now().plusDays(15));

		CreateCouponResponse response = CreateCouponResponse.builder().code("C123456789012345").build();

		given(commonCouponService.saveCategoryCoupon(any(CreateCouponCategoryRequest.class))).willReturn(response);

		mockMvc.perform(post("/coupons/category").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.code").value("C123456789012345"))
			.andDo(print());
	}

	@Test
	@DisplayName("카테고리 쿠폰 생성 유효성 검사 실패")
	public void saveCategoryCoupon_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
		CreateCouponCategoryRequest request = new CreateCouponCategoryRequest(null, "카테고리 쿠폰", 2000,
			DiscountType.PERCENT, 8000, 4000, LocalDateTime.now(), LocalDateTime.now().plusDays(15));

		mockMvc.perform(post("/coupons/category").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value("INVALID_REQUEST_ARGUMENT"))
			.andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.result.categoryId").value("카테고리 ID는 필수 입력 항목입니다."))
			.andDo(print());

		verify(commonCouponService, never()).saveCategoryCoupon(any(CreateCouponCategoryRequest.class));
	}

	@Test
	@DisplayName("쿠폰 만료 시간 업데이트 성공")
	public void updateCouponExpiration_ShouldReturnOk() throws Exception {
		UpdateCouponExpirationRequest request = new UpdateCouponExpirationRequest("C123456789012345");
		Coupon coupon = Coupon.builder()
			.code("C123456789012345")
			.name("테스트 쿠폰")
			.discountAmount(1000)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000)
			.minPurchaseAmount(10000)
			.startedAt(LocalDateTime.now().minusDays(1))
			.expirationAt(LocalDateTime.now().plusDays(10))
			.build();

		when(couponRepository.findById(any())).thenReturn(Optional.of(coupon));

		mockMvc.perform(put("/coupons/expiration").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk()).andDo(print());

		verify(commonCouponService).updateCouponExpiration(any(UpdateCouponExpirationRequest.class));
	}

	@Test
	@DisplayName("쿠폰 만료 시간 업데이트 유효성 검사 실패")
	public void updateCouponExpiration_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
		UpdateCouponExpirationRequest request = new UpdateCouponExpirationRequest("");

		mockMvc.perform(put("/coupons/expiration").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value("INVALID_REQUEST_ARGUMENT"))
			.andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.result.code").value("쿠폰 코드가 필요합니다."))
			.andDo(print());

		verify(commonCouponService, never()).updateCouponExpiration(any(UpdateCouponExpirationRequest.class));
	}

	@Test
	@DisplayName("일반 쿠폰 조회 성공")
	public void getCouponGeneralAll_ShouldReturnOk() throws Exception {
		Pageable pageable = PageRequest.of(0, 5);
		List<GetCouponResponse> couponList = List.of(
			new GetCouponResponse("G123456789012345", "일반 쿠폰 1", 1000, DiscountType.AMOUNT, 5000, 10000,
				LocalDateTime.now(), LocalDateTime.now().plusDays(10))
		);
		Page<GetCouponResponse> page = new PageImpl<>(couponList, pageable, couponList.size());

		given(commonCouponService.getCouponGeneralAll(any(Pageable.class))).willReturn(page);

		mockMvc.perform(get("/coupons/general").param("page", "0").param("size", "5"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content", hasSize(1)))
			.andExpect(jsonPath("$.content[0].code").value("G123456789012345"))
			.andExpect(jsonPath("$.content[0].name").value("일반 쿠폰 1"));
	}

	@Test
	@DisplayName("책 쿠폰 조회 성공")
	public void getCouponBookAll_ShouldReturnOk() throws Exception {
		Pageable pageable = PageRequest.of(0, 5);
		List<GetCouponBookResponse> couponList = List.of(
			new GetCouponBookResponse(1L, "B123456789012345", "책 쿠폰 1", 1500, DiscountType.AMOUNT, 7000, 5000,
				LocalDateTime.now(), LocalDateTime.now().plusDays(20))
		);
		Page<GetCouponBookResponse> page = new PageImpl<>(couponList, pageable, couponList.size());

		given(commonCouponService.getCouponBookAll(any(Pageable.class))).willReturn(page);

		mockMvc.perform(get("/coupons/book").param("page", "0").param("size", "5"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content", hasSize(1)))
			.andExpect(jsonPath("$.content[0].code").value("B123456789012345"))
			.andExpect(jsonPath("$.content[0].name").value("책 쿠폰 1"))
			.andExpect(jsonPath("$.content[0].bookId").value(1L));
	}

	@Test
	@DisplayName("카테고리 쿠폰 조회 성공")
	public void getCouponCategoryAll_ShouldReturnOk() throws Exception {
		Pageable pageable = PageRequest.of(0, 5);
		List<GetCouponCategoryResponse> couponList = List.of(
			new GetCouponCategoryResponse(2L, "C123456789012345", "카테고리 쿠폰 1", 2000, DiscountType.PERCENT, 8000, 4000,
				LocalDateTime.now(), LocalDateTime.now().plusDays(15))
		);
		Page<GetCouponCategoryResponse> page = new PageImpl<>(couponList, pageable, couponList.size());

		given(commonCouponService.getCouponCategryAll(any(Pageable.class))).willReturn(page);

		mockMvc.perform(get("/coupons/category").param("page", "0").param("size", "5"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content", hasSize(1)))
			.andExpect(jsonPath("$.content[0].code").value("C123456789012345"))
			.andExpect(jsonPath("$.content[0].name").value("카테고리 쿠폰 1"))
			.andExpect(jsonPath("$.content[0].categoryId").value(2L));
	}
}
