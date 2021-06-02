package com.BOEC.service.dto;

import lombok.Data;

@Data
public class CustomerRespondDto extends UserRespondDto {
    private CartRespondDto cart;
}
