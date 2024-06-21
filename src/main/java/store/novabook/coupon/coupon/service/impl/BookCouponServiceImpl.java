package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.util.CouponCodeGenerator;
import store.novabook.coupon.coupon.domain.BookCoupon;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.CouponType;
import store.novabook.coupon.coupon.dto.request.CreateCouponBookRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookResponse;
import store.novabook.coupon.coupon.repository.BookCouponRepository;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.service.BookCouponService;

@Service
@RequiredArgsConstructor
@Transactional
public class BookCouponServiceImpl implements BookCouponService {

	private final CouponCodeGenerator codeGenerator;
	private final CouponRepository couponRepository;
	private final BookCouponRepository bookCouponRepository;

	@Override
	public CreateCouponResponse saveBookCoupon(CreateCouponBookRequest createCouponBookRequest) {
		Coupon coupon = Coupon.of(codeGenerator.generateUniqueCode(CouponType.BOOK), createCouponBookRequest);
		Coupon saved = couponRepository.save(coupon);
		bookCouponRepository.save(BookCoupon.of(saved, createCouponBookRequest.bookId()));
		return CreateCouponResponse.fromEntity(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetCouponBookResponse> getCouponBookAll(Pageable pageable) {
		Page<BookCoupon> bookCouponList = bookCouponRepository.findAll(pageable);
		return bookCouponList.map(GetCouponBookResponse::fromEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public GetCouponBookAllResponse getCouponBook(Long bookId) {
		List<BookCoupon> bookCouponList = Optional.ofNullable(
				bookCouponRepository.findAllByBookIdAndCouponExpirationAtAfterAndCouponStartedAtBefore(bookId,
					LocalDateTime.now(), LocalDateTime.now()))
			.orElseThrow();
		return GetCouponBookAllResponse.fromEntity(bookCouponList);
	}
}
