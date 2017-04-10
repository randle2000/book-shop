package com.sln.bshop.service.impl;

import org.springframework.stereotype.Service;

import com.sln.bshop.domain.OrderBilling;
import com.sln.bshop.domain.OrderShipping;
import com.sln.bshop.domain.UserBilling;
import com.sln.bshop.service.OrderBillingService;

@Service
public class OrderBillingServiceImpl implements OrderBillingService {
	
	public OrderBilling assignFromUserBilling(OrderBilling orderBilling, UserBilling userBilling) {
		orderBilling.setOrderBillingName(userBilling.getUserBillingName());
		orderBilling.setOrderBillingStreet1(userBilling.getUserBillingStreet1());
		orderBilling.setOrderBillingStreet2(userBilling.getUserBillingStreet2());
		orderBilling.setOrderBillingCity(userBilling.getUserBillingCity());
		orderBilling.setOrderBillingState(userBilling.getUserBillingState());
		//orderBilling.setOrderBillingCountry(userBilling.getUserBillingCountry());
		orderBilling.setOrderBillingZipcode(userBilling.getUserBillingZipcode());
		
		return orderBilling;
	}
	
	public OrderBilling assignFromOrderShipping(OrderBilling orderBilling, OrderShipping orderShipping) {
		orderBilling.setOrderBillingName(orderShipping.getOrderShippingName());
		orderBilling.setOrderBillingStreet1(orderShipping.getOrderShippingStreet1());
		orderBilling.setOrderBillingStreet2(orderShipping.getOrderShippingStreet2());
		orderBilling.setOrderBillingCity(orderShipping.getOrderShippingCity());
		orderBilling.setOrderBillingState(orderShipping.getOrderShippingState());
		//orderBilling.setOrderBillingCountry(orderShipping.getOrderShippingCountry());
		orderBilling.setOrderBillingZipcode(orderShipping.getOrderShippingZipcode());
		
		return orderBilling;
	}

}
