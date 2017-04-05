package com.sln.bshop.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sln.bshop.domain.UserShipping;

public interface UserShippingRepository extends CrudRepository<UserShipping, Long> {

	@Modifying
	@Query("update UserShipping us set us.userShippingDefault = ?1")
	void setAllDefaultShipping(boolean defaultShipping);

}
