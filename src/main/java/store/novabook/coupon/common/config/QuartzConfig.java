package store.novabook.coupon.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import store.novabook.coupon.common.jobs.CouponDistributionJob;

@Configuration
public class QuartzConfig {

	@Bean
	public JobDetailFactoryBean jobDetailFactoryBean() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		factory.setJobClass(CouponDistributionJob.class);
		factory.setDescription("Invoke Batch Job");
		factory.setDurability(true);
		return factory;
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setAutoStartup(true);
		return new SchedulerFactoryBean();
	}
}