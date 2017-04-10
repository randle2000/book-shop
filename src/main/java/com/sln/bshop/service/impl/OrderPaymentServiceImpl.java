package com.sln.bshop.service.impl;

import org.springframework.stereotype.Service;

import com.sln.bshop.domain.OrderPayment;
import com.sln.bshop.domain.UserPayment;
import com.sln.bshop.service.OrderPaymentService;

@Service
public class OrderPaymentServiceImpl implements OrderPaymentService {
	
	public OrderPayment assignFromUserPayment(OrderPayment orderPayment, UserPayment userPayment) {
		orderPayment.setType(userPayment.getType());
		orderPayment.setHolderName(userPayment.getHolderName());
		orderPayment.setCardNumber(userPayment.getCardNumber());
		orderPayment.setExpiryMonth(userPayment.getExpiryMonth());
		orderPayment.setExpiryYear(userPayment.getExpiryYear());
		orderPayment.setCvc(userPayment.getCvc());
		
		return orderPayment;
	}

}
