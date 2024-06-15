package com.interview.subscription.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Subscription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subscriptionid" ,unique = true)
	private Long subscriptionid;

	@ManyToOne
	@JoinColumn(name = "hotelid", nullable = false)
	private Hotel hotel;

	@Column(name = "startDate", nullable = false)
	private LocalDate startDate;

	@Column(name = "nextPayment", nullable = false)
	private LocalDate nextPayment;

	@Column(name = "endDate")
	private LocalDate endDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "term", nullable = false)
	private Term term;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status;

	@Column(name = "createdAt")
	private LocalDateTime createdAt;

	@Column(name = "updatedAt")
	private LocalDateTime updatedAt;

	public Long getSubscriptionID() {
		return subscriptionid;
	}

	public void setSubscriptionID(Long subscriptionID) {
		this.subscriptionid = subscriptionID;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getNextPayment() {
		return nextPayment;
	}

	public void setNextPayment(LocalDate nextPayment) {
		this.nextPayment = nextPayment;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

    @PrePersist
    protected void onCreate() {
    	createdAt = LocalDateTime.now();
    	updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
    	updatedAt = LocalDateTime.now();
    }
}
