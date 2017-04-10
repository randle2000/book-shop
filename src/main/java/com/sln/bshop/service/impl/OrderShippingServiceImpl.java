package com.sln.bshop.service.impl;

import org.springframework.stereotype.Service;

import com.sln.bshop.domain.OrderShipping;
import com.sln.bshop.domain.UserShipping;
import com.sln.bshop.service.OrderShippingService;

@Service
public class OrderShippingServiceImpl implements OrderShippingService {
	public OrderShipping assignFromUserShipping(OrderShipping orderShipping, UserShipping userShipping) {
		orderShipping.setOrderShippingName(userShipping.getUserShippingName());
		orderShipping.setOrderShippingStreet1(userShipping.getUserShippingStreet1());
		orderShipping.setOrderShippingStreet2(userShipping.getUserShippingStreet2());
		orderShipping.setOrderShippingCity(userShipping.getUserShippingCity());
		orderShipping.setOrderShippingState(userShipping.getUserShippingState());
		//orderShipping.setOrderShippingCountry(userShipping.getUserShippingCountry());
		orderShipping.setOrderShippingZipcode(userShipping.getUserShippingZipcode());
		
		return orderShipping;
	}
}
