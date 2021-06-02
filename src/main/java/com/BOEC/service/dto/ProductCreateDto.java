package com.BOEC.service.dto;

import lombok.Data;

@Data
public class ProductCreateDto {
    private double price;

    private String description;

    private float salePercent;

    private int itemId;
}
