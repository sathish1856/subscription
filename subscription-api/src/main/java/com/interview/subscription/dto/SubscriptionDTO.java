package com.interview.subscription.dto;

import com.interview.subscription.model.Hotel;

public class SubscriptionDTO {
	private Long subscriptionID;
	private Long hotelid;
	private String startDate;
	private String nextPayment;
	private String endDate;
	private String term;
	private String status;
	private Hotel hotel;
	

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public Long getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(Long subscriptionID) {
		this.subscriptionID = subscriptionID;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getNextPayment() {
		return nextPayment;
	}

	public void setNextPayment(String nextPayment) {
		this.nextPayment = nextPayment;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getHotelid() {
		return hotelid;
	}

	public void setHotelid(Long hotelid) {
		this.hotelid = hotelid;
	}

}
