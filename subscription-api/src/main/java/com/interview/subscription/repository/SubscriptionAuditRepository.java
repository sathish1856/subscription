package com.interview.subscription.repository;

import com.interview.subscription.model.SubscriptionAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionAuditRepository extends JpaRepository<SubscriptionAudit, Long> {

}

