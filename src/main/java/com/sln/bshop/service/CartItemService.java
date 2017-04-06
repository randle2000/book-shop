package com.sln.bshop.service;

import com.sln.bshop.domain.Book;
import com.sln.bshop.domain.CartItem;
import com.sln.bshop.domain.User;

public interface CartItemService {
	//List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem);
	
	CartItem addBookToCartItem(Book book, User user, int qty);
	
	CartItem findById(Long id);
	
	void removeCartItem(CartItem cartItem);
	
	/*CartItem save(CartItem cartItem);
	
	List<CartItem> findByOrder(Order order);*/
}
