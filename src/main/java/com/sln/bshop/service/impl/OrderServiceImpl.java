package com.sln.bshop.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sln.bshop.domain.Book;
import com.sln.bshop.domain.CartItem;
import com.sln.bshop.domain.Order;
import com.sln.bshop.domain.OrderBilling;
import com.sln.bshop.domain.OrderPayment;
import com.sln.bshop.domain.OrderShipping;
import com.sln.bshop.domain.ShoppingCart;
import com.sln.bshop.domain.User;
import com.sln.bshop.repository.OrderRepository;
import com.sln.bshop.service.CartItemService;
import com.sln.bshop.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	CartItemService cartItemService;
	
	public synchronized Order createOrder(ShoppingCart shoppingCart,
			OrderShipping orderShipping,
			OrderBilling orderBilling,
			OrderPayment orderPayment,
			String shippingMethod,
			User user) {
		
		Order order = new Order();
		order.setOrderBilling(orderBilling);
		order.setOrderShipping(orderShipping);
		order.setOrderPayment(orderPayment);
		order.setOrderStatus("created");
		order.setShippingMethod(shippingMethod);
		
		orderShipping.setOrder(order);
		orderBilling.setOrder(order);
		orderPayment.setOrder(order);
		
		//List<CartItem> cartItemList = shoppingCart.getCartItemList();
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		for(CartItem cartItem : cartItemList) {
			Book book = cartItem.getBook();
			cartItem.setOrder(order);
			book.setInStockNumber(book.getInStockNumber() - cartItem.getQty());
		}
		
		order.setCartItemList(cartItemList);
		order.setOrderDate(Calendar.getInstance().getTime());
		order.setOrderTotal(shoppingCart.getGrandTotal());
		order.setUser(user);
		order = orderRepository.save(order);
		
		return order;
	}
	
	public Order findOne(Long id) {
		return orderRepository.findOne(id);
	}

}
