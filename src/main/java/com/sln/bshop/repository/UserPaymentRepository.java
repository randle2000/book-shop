package com.sln.bshop.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sln.bshop.domain.User;
import com.sln.bshop.domain.UserPayment;

public interface UserPaymentRepository extends CrudRepository<UserPayment, Long>{
	
	@Modifying
	@Query("update UserPayment up set up.defaultPayment = ?1 where up.user = ?2")
	void setAllDefaultPayment(boolean defaultPayment, User user);

}
