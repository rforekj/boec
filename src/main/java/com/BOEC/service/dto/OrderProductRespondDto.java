package com.BOEC.service.dto;

import lombok.Data;

@Data
public class OrderProductRespondDto {
    private ProductRespondDto product;

    private int quantity;
}
