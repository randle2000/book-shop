package com.sln.bshop.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sln.bshop.domain.CartItem;
import com.sln.bshop.domain.Order;
import com.sln.bshop.domain.OrderBilling;
import com.sln.bshop.domain.OrderPayment;
import com.sln.bshop.domain.OrderShipping;
import com.sln.bshop.domain.ShoppingCart;
import com.sln.bshop.domain.User;
import com.sln.bshop.domain.UserPayment;
import com.sln.bshop.domain.UserShipping;
import com.sln.bshop.service.OrderBillingService;
import com.sln.bshop.service.OrderPaymentService;
import com.sln.bshop.service.OrderService;
import com.sln.bshop.service.OrderShippingService;
import com.sln.bshop.service.ShoppingCartService;
import com.sln.bshop.service.UserPaymentService;
import com.sln.bshop.service.UserService;
import com.sln.bshop.service.UserShippingService;
import com.sln.bshop.utility.MailConstructor;
import com.sln.bshop.utility.USConstants;

@Controller
public class CheckoutController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CheckoutController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private OrderShippingService orderShippingService;

	@Autowired
	private OrderBillingService orderBillingService;

	@Autowired
	private OrderPaymentService orderPaymentService;

	@Autowired
	private UserShippingService userShippingService;

	@Autowired
	private UserPaymentService userPaymentService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private MailConstructor mailConstructor;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	private void populateModel(User user, Model model) {
		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);
		model.addAttribute("emptyPaymentList", userPaymentList.size() == 0);
		model.addAttribute("emptyShippingList", userShippingList.size() == 0);
		
		ShoppingCart shoppingCart = user.getShoppingCart();
		List<CartItem> cartItemList = shoppingCart.getCartItemList(); 
		//model.addAttribute("shoppingCart", shoppingCart);
		model.addAttribute("cartItemList", cartItemList);

		List<String> stateList = USConstants.listOfUSStatesCode;
		model.addAttribute("stateList", stateList);
	}
	
	@RequestMapping("/checkout")
	public String checkout(@RequestParam("id") Long cartId,
			@RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField,
			Model model, Principal principal) {
		User user = userService.findByEmail(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();

		if (!cartId.equals(shoppingCart.getId())) {
			return "badRequest";
		}

		List<CartItem> cartItemList = shoppingCart.getCartItemList();

		if (cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppingCart/cart";
		}

		for (CartItem cartItem : cartItemList) {
			if (cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}

		OrderShipping orderShipping = new OrderShipping();
		OrderBilling orderBilling = new OrderBilling();
		OrderPayment orderPayment = new OrderPayment();

		UserShipping userShipping = userService.getDefaultUserShipping(user);
		if (userShipping != null) {
			orderShipping = orderShippingService.assignFromUserShipping(orderShipping, userShipping);
		}
		UserPayment userPayment = userService.getDefaultUserPayment(user);
		if (userPayment != null) {
			orderPayment = orderPaymentService.assignFromUserPayment(orderPayment, userPayment);
			orderBilling = orderBillingService.assignFromUserBilling(orderBilling, userPayment.getUserBilling());
		}
		model.addAttribute("orderShipping", orderShipping);
		model.addAttribute("orderPayment", orderPayment);
		model.addAttribute("orderBilling", orderBilling);

		model.addAttribute("classActiveShipping", true);

		if (missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}
		
		populateModel(user, model);
		return "checkout";
	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkoutPost(
			@Valid @ModelAttribute("orderShipping") OrderShipping orderShipping, BindingResult osResult,
			@Valid @ModelAttribute("orderBilling") OrderBilling orderBilling, BindingResult obResult,
			@Valid @ModelAttribute("orderPayment") OrderPayment orderPayment, BindingResult opResult,
			@RequestParam(value = "billingSameAsShipping", required = false) String billingSameAsShipping,
			@RequestParam("shippingMethod") String shippingMethod,
			Principal principal, Model model) {
		
		/*
		if (orderShipping.getShippingAddressStreet1().isEmpty() 
				|| orderShipping.getShippingAddressCity().isEmpty()
				|| orderShipping.getShippingAddressState().isEmpty()
				|| orderShipping.getShippingAddressName().isEmpty()
				|| orderShipping.getShippingAddressZipcode().isEmpty() 
				|| orderPayment.getCardNumber().isEmpty()
				|| orderPayment.getCvc() == 0 || orderBilling.getBillingAddressStreet1().isEmpty()
				|| orderBilling.getBillingAddressCity().isEmpty() 
				|| orderBilling.getBillingAddressState().isEmpty()
				|| orderBilling.getBillingAddressName().isEmpty()
				|| orderBilling.getBillingAddressZipcode().isEmpty())
			return "redirect:/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true";*/
		
		User user = userService.findByEmail(principal.getName());
		
		if (osResult.hasErrors()) {
			populateModel(user, model);
			model.addAttribute("missingRequiredField", true);
			model.addAttribute("classActiveShipping", true);
			return "checkout";
		}
		if (opResult.hasErrors()) {
			populateModel(user, model);
			model.addAttribute("missingRequiredField", true);
			model.addAttribute("classActivePayment", true);
			return "checkout";
		}
		if (billingSameAsShipping == null && obResult.hasErrors()) {
			populateModel(user, model);
			model.addAttribute("missingRequiredField", true);
			model.addAttribute("classActivePayment", true);
			return "checkout";
		}
		
		if (billingSameAsShipping != null) {
			orderBilling = orderBillingService.assignFromOrderShipping(orderBilling, orderShipping);
		}
		
		ShoppingCart shoppingCart = user.getShoppingCart(); 

		Order order = orderService.createOrder(shoppingCart, orderShipping, orderBilling, orderPayment, shippingMethod, user);
		
		shoppingCartService.clearShoppingCart(shoppingCart);
		
		MimeMessagePreparator emailMessage = mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH); 
		LOG.info("Sending email: {}", emailMessage);
		mailSender.send(emailMessage);
		
		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate;
		if (shippingMethod.equals("groundShipping")) {
			estimatedDeliveryDate = today.plusDays(5);
		} else {
			estimatedDeliveryDate = today.plusDays(3);
		}
		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);
		List<CartItem> cartItemList = order.getCartItemList();
		model.addAttribute("cartItemList", cartItemList);
		
		return "orderSubmittedPage";
	}

	// called when user clicks [Use this Address] on checkout page to use an address from his list 
	@RequestMapping("/setOrderShipping")
	public String setShippingAddress(@RequestParam("userShippingId") Long userShippingId, Principal principal, Model model) {
		User user = userService.findByEmail(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);
		ShoppingCart shoppingCart = user.getShoppingCart();

		if (!userShipping.getUser().getId().equals(user.getId())) {
			return "badRequest";
		} else {
			userService.setUserDefaultShipping(userShippingId, user);
			return "redirect:/checkout?id=" + shoppingCart.getId();
		}
	}
	
	// called when user clicks [Use this card] on checkout page to use a card from his list 
	@RequestMapping("/setOrderPayment")
	public String setPaymentMethod(@RequestParam("userPaymentId") Long userPaymentId, Principal principal, Model model) {
		User user = userService.findByEmail(principal.getName());
		UserPayment userPayment = userPaymentService.findById(userPaymentId);
		ShoppingCart shoppingCart = user.getShoppingCart();

		if (!userPayment.getUser().getId().equals(user.getId())) {
			return "badRequest";
		} else {
			userService.setUserDefaultPayment(userPaymentId, user);
			return "redirect:/checkout?id=" + shoppingCart.getId();
		}
	}

}
