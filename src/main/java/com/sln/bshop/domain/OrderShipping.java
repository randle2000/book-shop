package com.sln.bshop.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class OrderShipping {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Size(min=2, max=30, message="Invalid name")
	private String orderShippingName;
	
	@Size(min=3, max=30, message="Invalid address")
	private String orderShippingStreet1;
	
	private String orderShippingStreet2;
	
	@Size(min=2, max=30, message="Invalid address")
	private String orderShippingCity;
	
	@NotEmpty
	private String orderShippingState;
	
	//private String orderShippingCountry;
	
	@Digits(integer=8, fraction=0, message="Invalid ZIP code (must be digits)")
	@Size(min=3, max=8, message="Invalid ZIP code length")
	private String orderShippingZipcode;
	
	@OneToOne
	private Order order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderShippingName() {
		return orderShippingName;
	}

	public void setOrderShippingName(String orderShippingName) {
		this.orderShippingName = orderShippingName;
	}

	public String getOrderShippingStreet1() {
		return orderShippingStreet1;
	}

	public void setOrderShippingStreet1(String orderShippingStreet1) {
		this.orderShippingStreet1 = orderShippingStreet1;
	}

	public String getOrderShippingStreet2() {
		return orderShippingStreet2;
	}

	public void setOrderShippingStreet2(String orderShippingStreet2) {
		this.orderShippingStreet2 = orderShippingStreet2;
	}

	public String getOrderShippingCity() {
		return orderShippingCity;
	}

	public void setOrderShippingCity(String orderShippingCity) {
		this.orderShippingCity = orderShippingCity;
	}

	public String getOrderShippingState() {
		return orderShippingState;
	}

	public void setOrderShippingState(String orderShippingState) {
		this.orderShippingState = orderShippingState;
	}

	public String getOrderShippingZipcode() {
		return orderShippingZipcode;
	}

	public void setOrderShippingZipcode(String orderShippingZipcode) {
		this.orderShippingZipcode = orderShippingZipcode;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
