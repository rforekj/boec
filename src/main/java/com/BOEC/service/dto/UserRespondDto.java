package com.BOEC.service.dto;

import com.BOEC.model.Role;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

@Data
public class UserRespondDto {
    private Long id;

    private String name;

    private String phone;

    private String email;

    private String address;

    private Collection<String> roles;

}
