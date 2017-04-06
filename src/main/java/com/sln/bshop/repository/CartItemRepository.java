package com.sln.bshop.repository;

import org.springframework.data.repository.CrudRepository;
//import org.springframework.transaction.annotation.Transactional;

import com.sln.bshop.domain.CartItem;

//@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Long>{
	//List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	//List<CartItem> findByOrder(Order order);
}
