package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.common.util.CouponCodeGenerator;
import store.novabook.coupon.coupon.domain.BookCoupon;
import store.novabook.coupon.coupon.domain.CategoryCoupon;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.CouponType;
import store.novabook.coupon.coupon.dto.request.CreateCouponBookRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponCategoryRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.UpdateCouponExpirationRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.repository.BookCouponRepository;
import store.novabook.coupon.coupon.repository.CategoryCouponRepository;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.service.CouponService;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceImpl implements CouponService {

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
	public CreateCouponResponse saveBookCoupon(CreateCouponBookRequest createCouponBookRequest) {
		Coupon coupon = Coupon.of(codeGenerator.generateUniqueCode(CouponType.BOOK), createCouponBookRequest);
		Coupon saved = couponRepository.save(coupon);
		bookCouponRepository.save(BookCoupon.of(saved, createCouponBookRequest.bookId()));
		return CreateCouponResponse.fromEntity(saved);
	}

	@Override
	public CreateCouponResponse saveCategoryCoupon(CreateCouponCategoryRequest createCouponCategoryRequest) {
		Coupon coupon = Coupon.of(codeGenerator.generateUniqueCode(CouponType.CATEGORY), createCouponCategoryRequest);
		Coupon saved = couponRepository.save(coupon);
		categoryCouponRepository.save(CategoryCoupon.of(saved, createCouponCategoryRequest.categoryId()));
		return CreateCouponResponse.fromEntity(saved);
	}

	@Override
	public void updateCouponExpiration(UpdateCouponExpirationRequest request) {
		Coupon coupon = couponRepository.findById(request.code())
			.orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));
		coupon.updateExprationAt(LocalDateTime.now());
		couponRepository.save(coupon);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetCouponResponse> getCouponGeneralAll(Pageable pageable) {
		Page<Coupon> couponList = couponRepository.findAllByCodeStartsWith(CouponType.GENERAL.getPrefix(), pageable);
		return couponList.map(GetCouponResponse::fromEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetCouponBookResponse> getCouponBookAll(Pageable pageable) {
		Page<BookCoupon> bookCouponList = bookCouponRepository.findAll(pageable);
		return bookCouponList.map(GetCouponBookResponse::fromEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetCouponCategoryResponse> getCouponCategryAll(Pageable pageable) {
		Page<CategoryCoupon> categoryCouponList = categoryCouponRepository.findAll(pageable);
		return categoryCouponList.map(GetCouponCategoryResponse::fromEntity);
	}

	// 만료시간이 현재시간보다 후인 쿠폰들만 가져온다.
	@Override
	@Transactional(readOnly = true)
	public GetCouponBookAllResponse getCouponBook(Long bookId) {
		List<BookCoupon> bookCouponList = bookCouponRepository.findAllByBookIdAndCoupon_ExpirationAtAfter(bookId,
			LocalDateTime.now());
		return GetCouponBookAllResponse.fromEntity(bookCouponList);
	}

	@Override
	public GetCouponCategoryAllResponse getCouponCategory(Long categoryId) {
		List<CategoryCoupon> categoryCouponList = categoryCouponRepository.findAllByCategoryIdAndCoupon_ExpirationAtAfter(
			categoryId, LocalDateTime.now());
		return GetCouponCategoryAllResponse.fromEntity(categoryCouponList);
	}

}
