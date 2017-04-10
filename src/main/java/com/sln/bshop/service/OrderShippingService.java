package com.sln.bshop.service;

import com.sln.bshop.domain.OrderShipping;
import com.sln.bshop.domain.UserShipping;

public interface OrderShippingService {
	OrderShipping assignFromUserShipping(OrderShipping orderShipping, UserShipping userShipping);
}
