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
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.repository.CouponTemplateRepository;
import store.novabook.coupon.coupon.service.CouponSchedulerService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponSchedulerServiceImpl implements CouponSchedulerService {
	private final Scheduler scheduler;
	private final CouponTemplateRepository couponTemplateRepository;

	@PostConstruct
	public void init() throws SchedulerException {
		scheduler.start();
		rescheduleJobs();
	}

	public void rescheduleJobs() {
		List<CouponTemplate> couponTemplates = couponTemplateRepository.findAllByStartedAtAfter(LocalDateTime.now());
		for (CouponTemplate couponTemplate : couponTemplates) {
			scheduleCouponJob(couponTemplate);
		}
	}

	@Override
	public void scheduleCouponJob(CouponTemplate couponTemplate) {
		try {
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("couponCode", couponTemplate.getId());

			// job 정의
			JobDetail jobDetail = JobBuilder.newJob(CouponDistributionJob.class)
				.withIdentity("couponJob_" + couponTemplate.getId(), "couponJobs")
				.usingJobData(jobDataMap)
				.storeDurably()
				.build();

			// trigger 설정
			Trigger trigger = TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.withIdentity("couponTrigger_" + couponTemplate.getId(), "couponTriggers")
				.startAt(Date.from(couponTemplate.getStartedAt().atZone(ZoneId.systemDefault()).toInstant()))
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
				.build();
			scheduler.scheduleJob(jobDetail, trigger);
			log.info("Scheduled coupon job: {}", couponTemplate.getId());
		} catch (SchedulerException e) {
			log.error("Error occurred while scheduling coupon: {}", couponTemplate.getId(), e);
		}
	}

}
