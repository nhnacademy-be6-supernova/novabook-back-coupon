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

@Service
@RequiredArgsConstructor
@Transactional
public class BookCouponTemplateServiceImpl implements BookCouponTemplateService {

	private final BookCouponTemplateRepository bookCouponTemplateRepository;

	@Transactional(readOnly = true)
	@Override
	public Page<GetBookCouponTemplateResponse> findAll(Pageable pageable) {
		return bookCouponTemplateRepository.findAll(pageable).map(GetBookCouponTemplateResponse::fromEntity);
	}

	@Override
	public CreateCouponTemplateResponse create(CreateBookCouponTemplateRequest request) {
		BookCouponTemplate bookCouponTemplate = BookCouponTemplate.of(request);
		BookCouponTemplate saved = bookCouponTemplateRepository.save(bookCouponTemplate);
		return CreateCouponTemplateResponse.fromEntity(saved);
	}

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
