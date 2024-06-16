package com.interview.subscription.scheduler;

import com.interview.subscription.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExpiryScheduler {

    @Autowired
    private SubscriptionService subscriptionService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void runDailySubscriptionCheck() {
        subscriptionService.checkAndUpdateExpiredSubscriptions();
    }
}
