package com.sln.bshop;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.sln.bshop.domain.User;
import com.sln.bshop.domain.security.Role;
import com.sln.bshop.domain.security.UserRole;
import com.sln.bshop.service.UserService;
import com.sln.bshop.utility.SecurityUtility;

// @SpringBootApplication is the same as @Configuration @EnableAutoConfiguration @ComponentScan combined
// can use @SpringBootApplication(scanBasePackages={"com.sln.bshop"})
// (@EnableAutoConfiguration attempts to automatically configure your Spring application based on the jar dependencies that you have added)
@SpringBootApplication
// This will make properties from mail.properties also available (in addition to application-local.properties or application-heroku.properties 
// see: https://www.mkyong.com/spring/spring-propertysources-example/
@PropertySource("classpath:mail.properties")	
public class BookShopApplication implements CommandLineRunner {
	
	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(BookShopApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		User user1 = new User();
		user1.setFirstName("John");
		user1.setLastName("Galt");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("p"));
		user1.setEmail("jgalt@gmail.com");
		Set<UserRole> userRoles = new HashSet<>();
		Role role1= new Role();
		role1.setRoleId(1);
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
		
		// admin
		User user2 = new User();
		user2.setPassword(SecurityUtility.passwordEncoder().encode("admin"));
		user2.setEmail("admin@gmail.com");
		Set<UserRole> userRoles2 = new HashSet<>();
		Role role2= new Role();
		role2.setRoleId(0);
		role2.setName("ROLE_ADMIN");
		userRoles2.add(new UserRole(user2, role2));
		
		userService.createUser(user2, userRoles2);
	}
}
