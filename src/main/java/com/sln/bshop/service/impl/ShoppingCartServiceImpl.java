package com.sln.bshop.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sln.bshop.domain.CartItem;
import com.sln.bshop.domain.ShoppingCart;
import com.sln.bshop.repository.ShoppingCartRepository;
import com.sln.bshop.service.CartItemService;
import com.sln.bshop.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {
		BigDecimal cartTotal = new BigDecimal(0);
		
		List<CartItem> cartItemList = shoppingCart.getCartItemList();
		
		/*cartItemList.stream()
			.filter(cartItem -> cartItem.getBook().getInStockNumber() > 0)
			.forEach(cartItem -> {
				cartItemService.updateCartItem(cartItem);
				cartTotal = cartTotal.add(cartItem.getSubtotal());
			});*/
		for (CartItem cartItem : cartItemList) {
			if (cartItem.getBook().getInStockNumber() > 0) {
				cartItemService.updateCartItem(cartItem);
				cartTotal = cartTotal.add(cartItem.getSubtotal());
			}
		}
		
		shoppingCart.setGrandTotal(cartTotal);
		
		shoppingCartRepository.save(shoppingCart);
		
		return shoppingCart;
	}
	
	/*public void clearShoppingCart(ShoppingCart shoppingCart) {
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		for (CartItem cartItem : cartItemList) {
			cartItem.setShoppingCart(null);
			cartItemService.save(cartItem);
		}
		
		shoppingCart.setGrandTotal(new BigDecimal(0));
		
		shoppingCartRepository.save(shoppingCart);
	}*/

}
