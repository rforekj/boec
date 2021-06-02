package com.BOEC.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartRespondDto {
    private List<CartProductRespondDto> cartProducts;
}
