package com.BOEC.service.dto;

import com.BOEC.model.processing.Product;
import lombok.Data;

@Data
public class CartProductRespondDto {
    private ProductRespondDto product;

    private int quantity;
}
