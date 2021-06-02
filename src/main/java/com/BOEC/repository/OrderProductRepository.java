package com.BOEC.repository;

import com.BOEC.model.processing.order.OrderProduct;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderProductRepository extends CrudRepository<OrderProduct, Integer> {
    Optional<OrderProduct> findByOrder_IdAndProduct_Id(int orderId, int productId);
}
