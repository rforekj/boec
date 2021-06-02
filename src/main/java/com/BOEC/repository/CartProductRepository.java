package com.BOEC.repository;

import com.BOEC.model.processing.cart.CartProduct;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends CrudRepository<CartProduct, Integer> {
    Optional<CartProduct> findByCart_IdAndProduct_Id(int cartId, int productId);
    List<CartProduct> findByProduct_Id(int productId);
    void deleteByCart_IdAndProduct_Id(int cartId, int productId);
    void deleteByProduct_Id(int productId);

    @Query(value = "delete from cart_product where cart_id=:cartId", nativeQuery = true)
    @Modifying
    void deleteByCart_Id(@Param("cartId") int cartId);
}
