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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon/templates/book")
public class BookCouponTemplateController implements BookCouponTemplateControllerDocs {

	private final BookCouponTemplateService bookCouponTemplateService;

	@GetMapping
	public ResponseEntity<Page<GetBookCouponTemplateResponse>> getBookCouponTemplateAll(Pageable pageable) {
		Page<GetBookCouponTemplateResponse> response = bookCouponTemplateService.findAll(pageable);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<CreateCouponTemplateResponse> createBookCouponTemplate(
		@Valid @RequestBody CreateBookCouponTemplateRequest request) {
		CreateCouponTemplateResponse response = bookCouponTemplateService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{bookId}")
	public ResponseEntity<GetBookCouponTemplateAllResponse> getCouponTemplateByBookId(@PathVariable Long bookId,
		@RequestParam(defaultValue = "true") boolean isValid) {
		GetBookCouponTemplateAllResponse response = bookCouponTemplateService.findAllByBookId(bookId, isValid);
		return ResponseEntity.ok(response);
	}
}
