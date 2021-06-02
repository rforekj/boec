package com.BOEC.service.dto;

import lombok.Data;

@Data
public class ProductRespondDto {

    private int id;

    private double price;

    private String description;

    private float salePercent;

    private ItemRespondDto item;
}
