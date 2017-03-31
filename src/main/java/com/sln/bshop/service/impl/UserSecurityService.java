package com.sln.bshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sln.bshop.domain.User;
import com.sln.bshop.repository.UserRepository;

@Service
public class UserSecurityService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		
		if (user == null)
			throw new UsernameNotFoundException("Username not found");
		
		return user;
		
	}

}
