package store.novabook.coupon.common.aop;

import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class TimeTraceAspect {

	@Around("@annotation(timeTrace)")
	public Object measureExecutionTime(ProceedingJoinPoint joinPoint, TimeTrace timeTrace) throws Throwable {
		long startTime = System.currentTimeMillis();
		log.info("메소드 {} 실행 시작 시간: {}", joinPoint.getSignature(), LocalDateTime.now());

		Object proceed = joinPoint.proceed();

		long endTime = System.currentTimeMillis();
		log.info("메소드 {} 실행 종료 시간: {}. 총 소요 시간: {} ms", joinPoint.getSignature(), LocalDateTime.now(),
			(endTime - startTime));

		return proceed;
	}
}
