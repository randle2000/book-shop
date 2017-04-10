package com.sln.bshop.service;

import com.sln.bshop.domain.ShoppingCart;

public interface ShoppingCartService {
	ShoppingCart updateShoppingCart(ShoppingCart shoppingCart);
	
	void clearShoppingCart(ShoppingCart shoppingCart);
}
