package com.BOEC.repository;

import com.BOEC.model.processing.order.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findByCustomer_IdOrderByCreatedDateDesc(Long id);
}
