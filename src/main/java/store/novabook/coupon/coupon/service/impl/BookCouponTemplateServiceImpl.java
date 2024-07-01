package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateBookCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetBookCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetBookCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.BookCouponTemplate;
import store.novabook.coupon.coupon.repository.BookCouponTemplateRepository;
import store.novabook.coupon.coupon.service.BookCouponTemplateService;

/**
 * {@code BookCouponTemplateServiceImpl} 클래스는 도서 쿠폰 템플릿에 대한 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BookCouponTemplateServiceImpl implements BookCouponTemplateService {

	private final BookCouponTemplateRepository bookCouponTemplateRepository;

	/**
	 * 모든 도서 쿠폰 템플릿을 페이지 형식으로 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 모든 도서 쿠폰 템플릿의 페이지
	 */
	@Transactional(readOnly = true)
	@Override
	public Page<GetBookCouponTemplateResponse> findAll(Pageable pageable) {
		return bookCouponTemplateRepository.findAll(pageable).map(GetBookCouponTemplateResponse::fromEntity);
	}

	/**
	 * 새로운 도서 쿠폰 템플릿을 생성합니다.
	 *
	 * @param request 도서 쿠폰 템플릿 생성 요청
	 * @return 생성된 도서 쿠폰 템플릿의 응답
	 */
	@Override
	public CreateCouponTemplateResponse create(CreateBookCouponTemplateRequest request) {
		BookCouponTemplate bookCouponTemplate = BookCouponTemplate.of(request);
		BookCouponTemplate saved = bookCouponTemplateRepository.save(bookCouponTemplate);
		return CreateCouponTemplateResponse.fromEntity(saved);
	}

	/**
	 * 주어진 도서 ID와 유효성 여부에 따라 모든 도서 쿠폰 템플릿을 조회합니다.
	 *
	 * @param bookId  도서 ID
	 * @param isValid 유효성 여부
	 * @return 주어진 조건에 맞는 도서 쿠폰 템플릿의 응답
	 */
	@Transactional(readOnly = true)
	@Override
	public GetBookCouponTemplateAllResponse findAllByBookId(Long bookId, boolean isValid) {
		if (isValid) {
			List<BookCouponTemplate> templateList = bookCouponTemplateRepository.findAllByBookIdAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
				bookId, LocalDateTime.now(), LocalDateTime.now());
			return GetBookCouponTemplateAllResponse.fromEntity(templateList);
		}
		List<BookCouponTemplate> templateList = bookCouponTemplateRepository.findAllByBookId(bookId);
		return GetBookCouponTemplateAllResponse.fromEntity(templateList);
	}
}
