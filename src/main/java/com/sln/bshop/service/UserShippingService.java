package com.sln.bshop.service;

import com.sln.bshop.domain.UserShipping;

public interface UserShippingService {
	UserShipping findById(Long id);
	
	void removeById(Long id);
}
