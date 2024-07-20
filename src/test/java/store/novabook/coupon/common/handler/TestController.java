package store.novabook.coupon.common.handler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.ForbiddenException;
import store.novabook.coupon.common.exception.InternalServerException;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.common.exception.NovaException;

@RestController
public class TestController {

	@GetMapping("/test/not-found")
	public void throwNotFoundException() {
		throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
	}

	@GetMapping("/test/forbidden")
	public void throwForbiddenException() {
		throw new ForbiddenException(ErrorCode.FORBIDDEN);
	}

	@GetMapping("/test/nova-exception")
	public void throwNovaException() {
		throw new NovaException(ErrorCode.INVALID_REQUEST_ARGUMENT) {
		};
	}

	@GetMapping("/test/exception")
	public void throwException() {
		throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/test/valid")
	public void validTest(@RequestBody @Valid TestRequest request) {
	}

	@GetMapping("/test/type-mismatch")
	public void typeMismatch(@RequestParam int value) {
	}
}
