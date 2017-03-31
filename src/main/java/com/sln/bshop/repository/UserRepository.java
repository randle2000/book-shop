package com.sln.bshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.sln.bshop.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByEmail(String email);

}
