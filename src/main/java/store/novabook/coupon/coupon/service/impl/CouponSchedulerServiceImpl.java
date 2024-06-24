package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.coupon.common.jobs.CouponDistributionJob;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.service.CouponSchedulerService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponSchedulerServiceImpl implements CouponSchedulerService {
	private final Scheduler scheduler;
	private final CouponRepository couponRepository;

	@PostConstruct
	public void init() throws SchedulerException {
		scheduler.start();
		rescheduleJobs();
	}

	public void rescheduleJobs() {
		List<Coupon> coupons = couponRepository.findAllByStartedAtAfter(LocalDateTime.now());
		for (Coupon coupon : coupons) {
			scheduleCouponJob(coupon);
		}
	}

	@Override
	public void scheduleCouponJob(Coupon coupon) {
		try {
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("couponCode", coupon.getCode());

			// job 정의
			JobDetail jobDetail = JobBuilder.newJob(CouponDistributionJob.class)
				.withIdentity("couponJob_" + coupon.getCode(), "couponJobs")
				.usingJobData(jobDataMap)
				.storeDurably()
				.build();

			// trigger 설정
			Trigger trigger = TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.withIdentity("couponTrigger_" + coupon.getCode(), "couponTriggers")
				.startAt(Date.from(coupon.getStartedAt().atZone(ZoneId.systemDefault()).toInstant()))
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
				.build();
			scheduler.scheduleJob(jobDetail, trigger);
			log.info("Scheduled coupon job: {}", coupon.getCode());
		} catch (SchedulerException e) {
			log.error("Error occurred while scheduling coupon: {}", coupon.getCode(), e);
		}
	}

}
