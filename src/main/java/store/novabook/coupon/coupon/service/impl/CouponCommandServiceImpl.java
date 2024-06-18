package store.novabook.coupon.coupon.service.impl;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.domain.BookCoupon;
import store.novabook.coupon.coupon.domain.CategoryCoupon;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.CouponType;
import store.novabook.coupon.coupon.dto.request.CreateBookCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.repository.BookCouponRepository;
import store.novabook.coupon.coupon.repository.CategoryCouponRepository;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.service.CouponCommandService;
import store.novabook.coupon.util.CouponCodeGenerator;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponCommandServiceImpl implements CouponCommandService {

	private final CouponCodeGenerator codeGenerator;

	private final CouponRepository couponRepository;
	private final BookCouponRepository bookCouponRepository;
	private final CategoryCouponRepository categoryCouponRepository;

	@Override
	public CreateCouponResponse saveGeneralCoupon(CreateCouponRequest createCouponRequest) {
		Coupon coupon = Coupon.of(codeGenerator.generateUniqueCode(CouponType.GENERAL), createCouponRequest);
		Coupon saved = couponRepository.save(coupon);
		return CreateCouponResponse.fromEntity(saved);
	}

	@Override
	public CreateCouponResponse saveBookCoupon(CreateBookCouponRequest createBookCouponRequest) {
		Coupon coupon = Coupon.of(codeGenerator.generateUniqueCode(CouponType.BOOK), createBookCouponRequest);
		Coupon saved = couponRepository.save(coupon);
		bookCouponRepository.save(BookCoupon.of(saved, createBookCouponRequest.bookId()));
		return CreateCouponResponse.fromEntity(saved);
	}

	@Override
	public CreateCouponResponse saveCategoryCoupon(CreateCategoryCouponRequest createCategoryCouponRequest) {
		Coupon coupon = Coupon.of(codeGenerator.generateUniqueCode(CouponType.CATEGORY), createCategoryCouponRequest);
		Coupon saved = couponRepository.save(coupon);
		categoryCouponRepository.save(CategoryCoupon.of(saved, createCategoryCouponRequest.categoryId()));
		return CreateCouponResponse.fromEntity(saved);
	}
}
