package com.interview.subscription.repository;

import com.interview.subscription.model.SubscriptionAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionAuditRepository extends JpaRepository<SubscriptionAudit, Long> {

    @Query( value="select a from SubscriptionAudit a where a.subscription.subscriptionid = :subscriptionid")
    List<SubscriptionAudit> findBysubscriptionid(Long subscriptionid);
}

