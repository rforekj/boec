package com.BOEC.service.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;


@Data
public class UserRegistrationDto {

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotBlank(message = "password must not be blank")
    private String password;

    @NotBlank(message = "phone must not be blank")
    private String phone;

    @NotBlank(message = "address must not be blank")
    private String address;

    @Email
    @NotBlank(message = "email must not be blank")
    @Size(min = 3, max = 100)
    private String email;

    @NotNull(message = "role must not be blank")
    @Size(min = 1, max = 3, message = "Number of role must greater than 0 and lower than 4")
    private Set<String> roles;

}
