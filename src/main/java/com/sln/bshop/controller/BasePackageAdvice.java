package com.sln.bshop.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sln.bshop.domain.ShoppingCart;
import com.sln.bshop.domain.User;
import com.sln.bshop.service.UserService;

//Target all Controllers within specific packages
// see: http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#mvc-ann-controller-advice
@ControllerAdvice("com.sln.bshop.controller")
public class BasePackageAdvice {

	@Autowired
	UserService userService;
	
	@ModelAttribute
	public void populateModel(Model model, Principal principal) {
		if (principal != null) {
			User user = userService.findByEmail(principal.getName());
			ShoppingCart shoppingCart = user.getShoppingCart();
			model.addAttribute("user", user);
			model.addAttribute("shoppingCart", shoppingCart);
		}
		
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		if (auth != null) {
//			if (!(auth instanceof AnonymousAuthenticationToken)) {
//				User user = (User) auth.getPrincipal();
//				ShoppingCart shoppingCart = user.getShoppingCart();
//				model.addAttribute("user", user);
//				model.addAttribute("shoppingCart", shoppingCart);
//				
//			}
//		}
	}
	
}
