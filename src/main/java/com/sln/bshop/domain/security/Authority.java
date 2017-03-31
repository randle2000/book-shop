package com.sln.bshop.domain.security;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
	private static final long serialVersionUID = -6679252173908049485L;
	
	private final String authority;
	
	public Authority(String authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return authority;
	}

}
