package com.sln.bshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.sln.bshop.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
