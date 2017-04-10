package com.sln.bshop.service.impl;

import java.math.BigDecimal;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sln.bshop.domain.Book;
import com.sln.bshop.domain.CartItem;
import com.sln.bshop.domain.Order;
import com.sln.bshop.domain.ShoppingCart;
import com.sln.bshop.domain.User;
import com.sln.bshop.repository.CartItemRepository;
import com.sln.bshop.service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService {
	
	//private static final Logger LOG = LoggerFactory.getLogger(CartItemService.class);
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		return cartItemRepository.findByShoppingCart(shoppingCart);
	}
	
	public CartItem updateCartItem(CartItem cartItem) {
		BigDecimal bigDecimal = new BigDecimal(cartItem.getBook().getOurPrice()).multiply(new BigDecimal(cartItem.getQty()));
		
		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		cartItem.setSubtotal(bigDecimal);
		
		cartItemRepository.save(cartItem);
		
		return cartItem;
	}
	
	public CartItem addBookToCartItem(Book book, User user, int qty) {
		List<CartItem> cartItemList = user.getShoppingCart().getCartItemList();
		Long bookId = book.getId();
		for (CartItem cartItem : cartItemList) {
			if (bookId.equals(cartItem.getBook().getId())) {	// this book is already present in shopping cart
				cartItem.setQty(cartItem.getQty() + qty);
				cartItem.setSubtotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(cartItem.getQty())));
				cartItemRepository.save(cartItem);
				return cartItem;
			}
		}
		
		CartItem cartItem = new CartItem();
		ShoppingCart shoppingCart = user.getShoppingCart();
		cartItem.setShoppingCart(shoppingCart);
		cartItem.setBook(book);
		shoppingCart.getCartItemList().add(cartItem);
		
		cartItem.setQty(qty);
		cartItem.setSubtotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
		book.getCartItemList().add(cartItem); 
		//shoppingCart.getCartItemList().add(cartItem);
		cartItem = cartItemRepository.save(cartItem);
		
		return cartItem;
	}
	
	public CartItem findById(Long id) {
		return cartItemRepository.findOne(id);
	}
	
	public void removeCartItem(CartItem cartItem) {
		//bookToCartItemRepository.deleteByCartItem(cartItem);
		cartItemRepository.delete(cartItem);
	}
/*	
	public CartItem save(CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}
*/

	public List<CartItem> findByOrder(Order order) {
		return cartItemRepository.findByOrder(order);
	}
}
