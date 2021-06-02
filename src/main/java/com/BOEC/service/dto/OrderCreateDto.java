package com.BOEC.service.dto;

import lombok.Data;

@Data
public class OrderCreateDto {

    private String shippingAddress;

    private int shipmentId;

    private String phone;

}
