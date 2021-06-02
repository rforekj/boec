package com.BOEC.service.dto;

import com.BOEC.model.processing.order.Order;
import com.BOEC.model.processing.order.OrderProduct;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderRespondDto {
    private int id;

    private double total;

    private ShipmentRespondDto shipment;

    private String shippingAddress;

    private Order.OrderStatus status;

    private List<OrderProductRespondDto> orderProducts;

    private Date createdDate;

    private String customerName;

    private String phone;

}
