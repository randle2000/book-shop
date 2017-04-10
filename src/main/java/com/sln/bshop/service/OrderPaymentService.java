package com.sln.bshop.service;

import com.sln.bshop.domain.OrderPayment;
import com.sln.bshop.domain.UserPayment;

public interface OrderPaymentService {
	OrderPayment assignFromUserPayment(OrderPayment orderPayment, UserPayment userPayment);
}
