package com.BOEC.service.dto;

import lombok.Data;

@Data
public class ElectronicRespondDto extends ItemRespondDto {
    private String manufacturer;
    private String manufacturedYear;
}
