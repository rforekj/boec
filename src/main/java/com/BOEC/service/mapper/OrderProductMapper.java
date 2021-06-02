package com.BOEC.service.mapper;

import com.BOEC.model.processing.order.OrderProduct;
import com.BOEC.model.processing.order.Order;
import com.BOEC.model.processing.order.OrderProduct;
import com.BOEC.service.dto.OrderProductRespondDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderProductMapper {
    @Autowired
    ProductMapper productMapper;
    OrderProductRespondDto orderProductToOrderProductDto(OrderProduct orderProduct) {
        OrderProductRespondDto orderProductRespondDto = new OrderProductRespondDto();
        orderProductRespondDto.setProduct(productMapper.productToProductDto(orderProduct.getProduct()));
        orderProductRespondDto.setQuantity(orderProduct.getQuantity());
        return orderProductRespondDto;
    }

    List<OrderProductRespondDto> orderProductsToOrderProductDtos(List<OrderProduct> orderProducts) {
        return orderProducts.stream().map(this::orderProductToOrderProductDto).collect(Collectors.toList());
    }
    
}