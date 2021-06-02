package com.BOEC.service.dto;

import lombok.Data;

@Data
public class ShipmentRespondDto {
    private int id;

    private String shippingCompany;

    private String shippingTime;

    private double shippingCost;

    private ShipmentType type;

    public enum ShipmentType {
        FAST_SHIP,
        NORMAL_SHIP
    }
}
