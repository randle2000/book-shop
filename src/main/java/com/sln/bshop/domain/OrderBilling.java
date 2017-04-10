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
public class OrderBilling {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Size(min=2, max=30, message="Invalid name")
	private String orderBillingName;
	
	@Size(min=3, max=30, message="Invalid address")
	private String orderBillingStreet1;
	
	private String orderBillingStreet2;
	
	@Size(min=2, max=30, message="Invalid address")
	private String orderBillingCity;
	
	@NotEmpty
	private String orderBillingState;
	
	//private String orderBillingCountry;
	
	@Digits(integer=8, fraction=0, message="Invalid ZIP code (must be digits)")
	@Size(min=3, max=8, message="Invalid ZIP code length")
	private String orderBillingZipcode;
	
	@OneToOne
	private Order order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderBillingName() {
		return orderBillingName;
	}

	public void setOrderBillingName(String orderBillingName) {
		this.orderBillingName = orderBillingName;
	}

	public String getOrderBillingStreet1() {
		return orderBillingStreet1;
	}

	public void setOrderBillingStreet1(String orderBillingStreet1) {
		this.orderBillingStreet1 = orderBillingStreet1;
	}

	public String getOrderBillingStreet2() {
		return orderBillingStreet2;
	}

	public void setOrderBillingStreet2(String orderBillingStreet2) {
		this.orderBillingStreet2 = orderBillingStreet2;
	}

	public String getOrderBillingCity() {
		return orderBillingCity;
	}

	public void setOrderBillingCity(String orderBillingCity) {
		this.orderBillingCity = orderBillingCity;
	}

	public String getOrderBillingState() {
		return orderBillingState;
	}

	public void setOrderBillingState(String orderBillingState) {
		this.orderBillingState = orderBillingState;
	}

	public String getOrderBillingZipcode() {
		return orderBillingZipcode;
	}

	public void setOrderBillingZipcode(String orderBillingZipcode) {
		this.orderBillingZipcode = orderBillingZipcode;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	
}
