package com.sln.bshop.service;

import java.util.Set;

import com.sln.bshop.domain.User;
import com.sln.bshop.domain.security.PasswordResetToken;
import com.sln.bshop.domain.security.UserRole;

public interface UserService {
	PasswordResetToken getPasswordResetToken(String token);
	
	void createPasswordResetTokenForUser(User user, String token);
	
	User findByEmail(String email);
	
	User findById(Long id);
	
	User createUser(User user, Set<UserRole> userRoles);
	
	User createUser(String email, String password, String RoleName);
	
	User save(User user);
	
	/*void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);
	
	void updateUserShipping(UserShipping userShipping, User user);
	
	void setUserDefaultPayment(Long userPaymentId, User user);
	
	void setUserDefaultShipping(Long userShippingId, User user);*/
	
}
