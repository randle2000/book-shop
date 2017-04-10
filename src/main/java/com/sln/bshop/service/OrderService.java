package com.sln.bshop.service;

import com.sln.bshop.domain.Order;
import com.sln.bshop.domain.OrderBilling;
import com.sln.bshop.domain.OrderPayment;
import com.sln.bshop.domain.OrderShipping;
import com.sln.bshop.domain.ShoppingCart;
import com.sln.bshop.domain.User;

public interface OrderService {
	Order createOrder(ShoppingCart shoppingCart,
			OrderShipping orderShipping,
			OrderBilling orderBilling,
			OrderPayment orderPayment,
			String shippingMethod,
			User user);
	
	Order findOne(Long id);

}
