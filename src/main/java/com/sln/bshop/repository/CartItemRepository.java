package com.sln.bshop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
//import org.springframework.transaction.annotation.Transactional;

import com.sln.bshop.domain.CartItem;
import com.sln.bshop.domain.Order;
import com.sln.bshop.domain.ShoppingCart;

//@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Long>{
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	List<CartItem> findByOrder(Order order);
}
