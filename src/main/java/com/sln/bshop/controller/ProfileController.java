package com.sln.bshop.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sln.bshop.domain.CartItem;
import com.sln.bshop.domain.Order;
import com.sln.bshop.domain.User;
import com.sln.bshop.domain.UserBilling;
import com.sln.bshop.domain.UserPayment;
import com.sln.bshop.domain.UserShipping;
import com.sln.bshop.service.CartItemService;
import com.sln.bshop.service.OrderService;
import com.sln.bshop.service.UserPaymentService;
import com.sln.bshop.service.UserService;
import com.sln.bshop.service.UserShippingService;
import com.sln.bshop.service.impl.UserSecurityService;
import com.sln.bshop.utility.SecurityUtility;
import com.sln.bshop.utility.USConstants;

// this controller is only used for secure area
@Controller
public class ProfileController {
	
	//private static final Logger LOG = LoggerFactory.getLogger(ProfileController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserSecurityService userSecurityService;
	
	@Autowired
	UserPaymentService userPaymentService;
	
	@Autowired
	UserShippingService userShippingService;
	
	@Autowired
	CartItemService cartItemService;
	
	@Autowired
	OrderService orderService;
	
	private void doManualLogin(String username) {
		UserDetails userDetails = userSecurityService.loadUserByUsername(username);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),	userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	// will be called on each request
	@ModelAttribute
	public void populateModel(Model model, Principal principal) {
		User user = userService.findByEmail(principal.getName());
		//userService.detachUser(user);
		// My Profile tab
		//model.addAttribute("user", user);
		// Orders tab
		model.addAttribute("orderList", user.getOrderList());
		// Billing tab
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		// Shinning tab
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("userShippingList", user.getUserShippingList());
		// US States
		List<String> stateList = USConstants.listOfUSStatesCode;
		model.addAttribute("stateList", stateList);
		// Cart list
		//model.addAttribute("cartItemList", user.getShoppingCart().getCartItemList());
	}	
	
	@RequestMapping("/myProfile")
	public String myProfile(Model model, Principal principal) {
		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);
		
		model.addAttribute("classActiveProfile", true);
		return "myProfile";
	}
	
	// if you specify @ModelAttribute("user") instead of @ModelAttribute("newuser") in the method parameter here,
	// Spring will take existing User object that was added to the model by populateModel() and re-write it's properties with data coming from the request 
	// and that User object is an attached entity, so the Spring will rewrite its properties
	// (for example it may set password property to "" if it was not filled by the user in the form)
	// But by using @ModelAttribute("newuser") Spring will create a new User object
	@RequestMapping(value="/updateUserInfo", method=RequestMethod.POST)
	public String updateUserInfo(@ModelAttribute("newuser") @Valid User user, BindingResult result, @RequestParam("newPassword") String newPassword, Model model) throws Exception {
		model.addAttribute("classActiveProfile", true);

		if (result.hasErrors()) {
			return "myProfile";
		}
		
		User currentUser = userService.findById(user.getId());
		
		if(currentUser == null) {
			throw new Exception("User not found");
		}
		
		// check if email already exists
		User otherUser = userService.findByEmail(user.getEmail()); 
		if (otherUser != null) {
			if(!otherUser.getId().equals(currentUser.getId())) {
				model.addAttribute("emailExists", true);
				return "myProfile";
			}
		}
		otherUser = null;
		
		// update password
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUser.getPassword();
			if (passwordEncoder.matches(user.getPassword(), dbPassword)) {
				currentUser.setPassword(passwordEncoder.encode(newPassword));
			} else {
				model.addAttribute("incorrectPassword", true);
				return "myProfile";
			}
		}
		
		currentUser.setFirstName(user.getFirstName());
		currentUser.setLastName(user.getLastName());
		//currentUser.setEmail(user.getEmail());
		userService.save(currentUser);
		
		model.addAttribute("updateSuccess", true);
		
		model.addAttribute("user", currentUser);

		doManualLogin(currentUser.getEmail());
		
		return "myProfile";
	}
	
	/*********** Credit Cards **********/
	
	@RequestMapping(value="/setDefaultPayment", method=RequestMethod.POST)
	public String setDefaultPayment(@RequestParam("defaultUserPaymentId") Long defaultPaymentId, Principal principal, Model model) {
		User user = userService.findByEmail(principal.getName());
		userService.setUserDefaultPayment(defaultPaymentId, user);
		
		model.addAttribute("classActiveBilling", true);
		return "myProfile";
	}

	@RequestMapping("/updateCreditCard")
	public String updateCreditCard(@RequestParam("id") Long creditCardId, Principal principal, Model model) {
		User user = userService.findByEmail(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);
		
		if(!user.getId().equals(userPayment.getUser().getId())) {
			return "redirect:/badRequest";
		} else {
			UserBilling userBilling = userPayment.getUserBilling();
			model.addAttribute("userPayment", userPayment);
			model.addAttribute("userBilling", userBilling);
			
			model.addAttribute("addNewCreditCard", true);
			model.addAttribute("listOfCreditCards", false);
			model.addAttribute("classActiveBilling", true);
			return "myProfile";
		}
	}
	
	@RequestMapping("/removeCreditCard")
	public String removeCreditCard(@RequestParam("id") Long creditCardId, Principal principal, Model model){
		User user = userService.findByEmail(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);
		
		if(!user.getId().equals(userPayment.getUser().getId())) {
			return "redirect://badRequest";
		} else {
			userService.removeUserPayment(user, userPayment);

			model.addAttribute("classActiveBilling", true);
			return "myProfile";
		}
	}
	
	@RequestMapping("/listOfCreditCards")
	public String listOfCreditCards(Model model) {
		model.addAttribute("classActiveBilling", true);
		return "myProfile";
	}
	
	@RequestMapping("/addNewCreditCard")
	public String addNewCreditCard(Model model, Principal principal){
		UserBilling userBilling = new UserBilling();
		UserPayment userPayment = new UserPayment();
		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);
		
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("listOfCreditCards", false);
		model.addAttribute("classActiveBilling", true);
		
		return "myProfile";
	}
	
	@RequestMapping(value="/addNewCreditCard", method=RequestMethod.POST)
	public String addNewCreditCard(
			@Valid @ModelAttribute("userPayment") UserPayment userPayment, BindingResult upResult,
			@Valid @ModelAttribute("userBilling") UserBilling userBilling, BindingResult ubResult,
			Principal principal, Model model) {
		
		if (upResult.hasErrors() || ubResult.hasErrors()) {
			model.addAttribute("addNewCreditCard", true);
			model.addAttribute("listOfCreditCards", false);
			model.addAttribute("classActiveBilling", true);
			return "myProfile";
		}
		
		User user = userService.findByEmail(principal.getName());
		userService.updateUserBilling(userBilling, userPayment, user);
		model.addAttribute("creditCardUpdated", true);		// flash box
		
		model.addAttribute("classActiveBilling", true);
		return "myProfile";
	}
	
	/*********** Shipping Addresses **********/
	
	@RequestMapping(value="/setDefaultShippingAddress", method=RequestMethod.POST)
	public String setDefaultShippingAddress(@ModelAttribute("defaultShippingAddressId") Long defaultShippingId, Principal principal, Model model) {
		User user = userService.findByEmail(principal.getName());
		userService.setUserDefaultShipping(defaultShippingId, user);
		
		model.addAttribute("classActiveShipping", true);
		return "myProfile";
	}
	
	@RequestMapping("/updateUserShipping")
	public String updateUserShipping(@ModelAttribute("id") Long shippingAddressId, Principal principal, Model model) {
		User user = userService.findByEmail(principal.getName());
		UserShipping userShipping = userShippingService.findById(shippingAddressId);
		
		if(!user.getId().equals(userShipping.getUser().getId())) {
			return "redirect:/badRequest";
		} else {
			model.addAttribute("userShipping", userShipping);
			
			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("listOfShippingAddresses", false);
			model.addAttribute("classActiveShipping", true);
			return "myProfile";
		}
	}
	
	@RequestMapping("/removeUserShipping")
	public String removeUserShipping(@ModelAttribute("id") Long userShippingId, Principal principal, Model model) {
		User user = userService.findByEmail(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);
		
		if(!user.getId().equals(userShipping.getUser().getId())) {
			return "redirect:/badRequest";
		} else {
			userService.removeUserShipping(user, userShipping);
			
			model.addAttribute("classActiveShipping", true);
			return "myProfile";
		}
	}	
	
	@RequestMapping("/listOfShippingAddresses")
	public String listOfShippingAddresses(Model model) {
		model.addAttribute("classActiveShipping", true);
		return "myProfile";
	}
	
	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(Model model) {
		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);
		
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("listOfShippingAddresses", false);
		model.addAttribute("classActiveShipping", true);
		return "myProfile";
	}
	
	@RequestMapping(value="/addNewShippingAddress", method=RequestMethod.POST)
	public String addNewShippingAddressPost(
			@Valid @ModelAttribute("userShipping") UserShipping userShipping, BindingResult result,
			Principal principal, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("listOfShippingAddresses", false);
			model.addAttribute("classActiveShipping", true);
			return "myProfile";
		}
		
		User user = userService.findByEmail(principal.getName());
		userService.updateUserShipping(userShipping, user);
		model.addAttribute("shippingAddressUpdated", true); 	// flash box
		
		model.addAttribute("classActiveShipping", true);
		return "myProfile";
	}
	
	@RequestMapping("/orderDetail")
	public String orderDetail(@RequestParam("id") Long orderId,	Principal principal, Model model){
		User user = userService.findByEmail(principal.getName());
		Order order = orderService.findOne(orderId);
		
		if(!order.getUser().getId().equals(user.getId())) {
			return "badRequest";
		} else {
			List<CartItem> cartItemList = cartItemService.findByOrder(order);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("order", order);
			model.addAttribute("classActiveOrders", true);
			model.addAttribute("displayOrderDetail", true);
			return "myProfile";
		}
	}
	

	
}
