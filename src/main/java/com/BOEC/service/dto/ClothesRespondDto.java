package com.BOEC.service.dto;

import lombok.Data;

@Data
public class ClothesRespondDto extends ItemRespondDto {
    private String brand;
    private String size;
    private String type;
}
