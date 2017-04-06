package com.sln.bshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.sln.bshop.domain.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {

}
