package store.novabook.coupon.coupon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.controller.docs.BookCouponTemplateControllerDocs;
import store.novabook.coupon.coupon.dto.request.CreateBookCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetBookCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetBookCouponTemplateResponse;
import store.novabook.coupon.coupon.service.BookCouponTemplateService;

/**
 * {@code BookCouponTemplateController} 클래스는 도서 쿠폰 템플릿 API를 처리합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon/templates/book")
public class BookCouponTemplateController implements BookCouponTemplateControllerDocs {

	private final BookCouponTemplateService bookCouponTemplateService;

	/**
	 * 모든 도서 쿠폰 템플릿을 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 도서 쿠폰 템플릿의 페이지 응답
	 */
	@GetMapping
	public ResponseEntity<Page<GetBookCouponTemplateResponse>> getBookCouponTemplateAll(Pageable pageable) {
		Page<GetBookCouponTemplateResponse> response = bookCouponTemplateService.findAll(pageable);
		return ResponseEntity.ok(response);
	}

	/**
	 * 새로운 도서 쿠폰 템플릿을 생성합니다.
	 *
	 * @param request 도서 쿠폰 템플릿 생성 요청
	 * @return 생성된 도서 쿠폰 템플릿의 응답
	 */
	@PostMapping
	public ResponseEntity<CreateCouponTemplateResponse> createBookCouponTemplate(
		@Valid @RequestBody CreateBookCouponTemplateRequest request) {
		CreateCouponTemplateResponse response = bookCouponTemplateService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * ID를 이용해 특정 도서의 쿠폰 템플릿을 조회합니다.
	 *
	 * @param bookId  조회할 도서의 ID
	 * @param isValid 유효성 여부
	 * @return 조회된 도서 쿠폰 템플릿의 응답
	 */
	@GetMapping("/{bookId}")
	public ResponseEntity<GetBookCouponTemplateAllResponse> getCouponTemplateByBookId(@PathVariable Long bookId,
		@RequestParam(defaultValue = "true") boolean isValid) {
		GetBookCouponTemplateAllResponse response = bookCouponTemplateService.findAllByBookId(bookId, isValid);
		return ResponseEntity.ok(response);
	}
}
