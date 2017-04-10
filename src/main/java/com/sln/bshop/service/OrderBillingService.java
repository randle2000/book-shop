package com.sln.bshop.service;

import com.sln.bshop.domain.OrderBilling;
import com.sln.bshop.domain.OrderShipping;
import com.sln.bshop.domain.UserBilling;

public interface OrderBillingService {
	OrderBilling assignFromUserBilling(OrderBilling orderBilling, UserBilling userBilling);
	
	OrderBilling assignFromOrderShipping(OrderBilling orderBilling, OrderShipping orderShipping);
}
