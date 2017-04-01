package com.sln.bshop.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class UserShipping {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull @Size(min=2, max=30, message="Invalid name")
	private String userShippingName;
	
	@NotNull @Size(min=3, max=30, message="Invalid address")
	private String userShippingStreet1;
	
	private String userShippingStreet2;
	
	@NotNull @Size(min=2, max=30, message="Invalid address")
	private String userShippingCity;
	
	@NotNull
	private String userShippingState;
	
	//private String userShippingCountry;
	
	@NotNull
	@Digits(integer=8, fraction=0, message="Invalid ZIP code (must be digits)")
	@Size(min=3, max=8, message="Invalid ZIP code length")
	private String userShippingZipcode;
	
	private boolean userShippingDefault;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUserShippingName() {
		return userShippingName;
	}


	public void setUserShippingName(String userShippingName) {
		this.userShippingName = userShippingName;
	}


	public String getUserShippingStreet1() {
		return userShippingStreet1;
	}


	public void setUserShippingStreet1(String userShippingStreet1) {
		this.userShippingStreet1 = userShippingStreet1;
	}


	public String getUserShippingStreet2() {
		return userShippingStreet2;
	}


	public void setUserShippingStreet2(String userShippingStreet2) {
		this.userShippingStreet2 = userShippingStreet2;
	}


	public String getUserShippingCity() {
		return userShippingCity;
	}


	public void setUserShippingCity(String userShippingCity) {
		this.userShippingCity = userShippingCity;
	}


	public String getUserShippingState() {
		return userShippingState;
	}


	public void setUserShippingState(String userShippingState) {
		this.userShippingState = userShippingState;
	}

	/*
	public String getUserShippingCountry() {
		return userShippingCountry;
	}


	public void setUserShippingCountry(String userShippingCountry) {
		this.userShippingCountry = userShippingCountry;
	}*/


	public String getUserShippingZipcode() {
		return userShippingZipcode;
	}


	public void setUserShippingZipcode(String userShippingZipcode) {
		this.userShippingZipcode = userShippingZipcode;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public boolean isUserShippingDefault() {
		return userShippingDefault;
	}


	public void setUserShippingDefault(boolean userShippingDefault) {
		this.userShippingDefault = userShippingDefault;
	}
	
	
}
