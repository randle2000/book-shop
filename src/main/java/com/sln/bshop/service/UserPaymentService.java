package com.sln.bshop.service;

import com.sln.bshop.domain.UserPayment;

public interface UserPaymentService {
	UserPayment findById(Long id);
	
	void removeById(Long id);
}
