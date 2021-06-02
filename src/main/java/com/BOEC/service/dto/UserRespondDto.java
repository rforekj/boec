package com.BOEC.service.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class UserRespondDto {
    private Long id;

    private String name;

    private String phone;

    private String email;

    private String address;

    private Collection<String> roles;

}
