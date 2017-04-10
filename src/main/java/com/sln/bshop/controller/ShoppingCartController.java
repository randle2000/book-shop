package com.sln.bshop.controller;

import java.security.Principal;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sln.bshop.domain.Book;
import com.sln.bshop.domain.CartItem;
import com.sln.bshop.domain.ShoppingCart;
import com.sln.bshop.domain.User;
import com.sln.bshop.service.BookService;
import com.sln.bshop.service.CartItemService;
import com.sln.bshop.service.ShoppingCartService;
import com.sln.bshop.service.UserService;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	
	//private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@ModelAttribute
	public void populateModel(Model model, Principal principal) {
		User user = userService.findByEmail(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();
		List<CartItem> cartItemList = shoppingCart.getCartItemList();
		model.addAttribute("cartItemList", cartItemList);
		//model.addAttribute("shoppingCart", shoppingCart);
	}
	
	@RequestMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		User user = userService.findByEmail(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();
		shoppingCartService.updateShoppingCart(shoppingCart);
		return "shoppingCart";
	}

	// posting from bootDetail.html
	@RequestMapping(value="/addItem", method=RequestMethod.POST)
	public String addItem(
			@ModelAttribute("book") Book book,
			@RequestParam("qty") String qty,
			Model model, Principal principal) {
		User user = userService.findByEmail(principal.getName());
		book = bookService.findOne(book.getId());
		
		if (Integer.parseInt(qty) > book.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/bookDetail?id="+book.getId();
		}
		
		cartItemService.addBookToCartItem(book, user, Integer.parseInt(qty));
		model.addAttribute("addBookSuccess", true);
		
		//model.addAttribute("cartItemList", user.getShoppingCart().getCartItemList());
		
		return "forward:/bookDetail?id="+book.getId();
	}
	
	// posted from shoppingCart.html, updating qty for each cartItem
	@RequestMapping(value="/updateCartItem", method=RequestMethod.POST)
	public String updateShoppingCart(@RequestParam("id") Long cartItemId, @RequestParam("qty") int qty) {
		CartItem cartItem = cartItemService.findById(cartItemId);
		cartItem.setQty(qty);
		cartItemService.updateCartItem(cartItem);
		
		return "forward:/shoppingCart/cart";
	}
	
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("id") Long id) {
		cartItemService.removeCartItem(cartItemService.findById(id));
		
		return "forward:/shoppingCart/cart";
	}
}
