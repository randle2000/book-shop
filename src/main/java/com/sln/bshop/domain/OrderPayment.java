package com.sln.bshop.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

@Entity
public class OrderPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String type;

	@Digits(integer=16, fraction=0, message="Invalid Credit Card number (expecting digits - no more validation by now)")
	@Size(min=16, max=16, message="Number of digits must be 16 - no more validation by now")
	private String cardNumber;
	
	@NotNull 
	private int expiryMonth;
	
	@NotNull 
	private int expiryYear;

	@Range(min = 0, max = 999, message="Invalid CVC code")
	private int cvc;

	@Size(min=2, max=30, message="Invalid name")
	private String holderName;
	
	@OneToOne
	private Order order;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getExpiryMonth() {
		return expiryMonth;
	}

	public void setExpiryMonth(int expiryMonth) {
		this.expiryMonth = expiryMonth;
	}

	public int getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(int expiryYear) {
		this.expiryYear = expiryYear;
	}

	public int getCvc() {
		return cvc;
	}

	public void setCvc(int cvc) {
		this.cvc = cvc;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	
}
