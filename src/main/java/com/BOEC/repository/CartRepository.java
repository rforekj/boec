package com.BOEC.repository;

import com.BOEC.model.processing.cart.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Integer> {
    Cart findByCustomer_Id(Long id);
}
