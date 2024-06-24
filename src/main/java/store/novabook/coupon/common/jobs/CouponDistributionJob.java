package store.novabook.coupon.common.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.adapter.BatchAdapter;

@Component
@RequiredArgsConstructor
public class CouponDistributionJob extends QuartzJobBean {

	private final BatchAdapter batchAdapter;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		String couponCode = context.getMergedJobDataMap().getString("couponCode");
		batchAdapter.startCouponDistribution(couponCode);
	}
}