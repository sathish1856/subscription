package com.interview.subscription.dto;

public class HotelDTO {
    private Long hotelid;
    private String hotelName;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
	public Long getHotelID() {
		return hotelid;
	}
	public void setHotelID(Long hotelid) {
		this.hotelid = hotelid;
	}
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

    // Getters and Setters...
}
