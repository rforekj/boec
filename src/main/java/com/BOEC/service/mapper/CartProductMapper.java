package com.BOEC.service.mapper;

import com.BOEC.model.processing.cart.CartProduct;
import com.BOEC.model.processing.order.Order;
import com.BOEC.model.processing.order.OrderProduct;
import com.BOEC.service.dto.CartProductRespondDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartProductMapper {
    @Autowired
    ProductMapper productMapper;
    CartProductRespondDto cartProductToCartProductDto(CartProduct cartProduct) {
        CartProductRespondDto cartProductRespondDto = new CartProductRespondDto();
        cartProductRespondDto.setProduct(productMapper.productToProductDto(cartProduct.getProduct()));
        cartProductRespondDto.setQuantity(cartProduct.getQuantity());
        return cartProductRespondDto;
    }

    List<CartProductRespondDto> cartProductsToCartProductDtos(List<CartProduct> cartProducts) {
        if(cartProducts!=null)
        return cartProducts.stream().map(this::cartProductToCartProductDto).collect(Collectors.toList());
        return null;
    }

    OrderProduct cartProductToOrderProduct(CartProduct cartProduct) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setQuantity(cartProduct.getQuantity());
        orderProduct.setProduct(cartProduct.getProduct());
        return orderProduct;
    }

    List<OrderProduct> cartProductsToOrderProducts(List<CartProduct> cartProducts) {
        return cartProducts.stream().map(this::cartProductToOrderProduct).collect(Collectors.toList());
    }
}
