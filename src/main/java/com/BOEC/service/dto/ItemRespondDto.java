package com.BOEC.service.dto;

import lombok.Data;

@Data
public class ItemRespondDto {
    private int id;
    private String name;
    private double price;
    private int numberInStock;
    private String description;
    private ItemImportDto.ItemType itemType;

    private String image;
    private boolean isPublish;
    private int productId;

}
