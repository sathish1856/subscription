package com.interview.subscription.dto;

import java.time.LocalDateTime;

public class SubscriptionAuditDTO {
	private Long auditID;
	private Long subscriptionID;
	private LocalDateTime changeDate;
	private String fieldChanged;
	private String oldValue;
	private String newValue;

	public Long getAuditID() {
		return auditID;
	}

	public void setAuditID(Long auditID) {
		this.auditID = auditID;
	}

	public Long getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(Long subscriptionID) {
		this.subscriptionID = subscriptionID;
	}

	public LocalDateTime getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(LocalDateTime changeDate) {
		this.changeDate = changeDate;
	}

	public String getFieldChanged() {
		return fieldChanged;
	}

	public void setFieldChanged(String fieldChanged) {
		this.fieldChanged = fieldChanged;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	// Getters and Setters...
}
