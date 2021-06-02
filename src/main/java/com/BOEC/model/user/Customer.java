package com.BOEC.model.user;

import com.BOEC.config.Constants;
import com.BOEC.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class Customer extends User {
    @Autowired
    RoleRepository roleRepository;

    public Customer() {
        super();
        this.setRoles(Arrays.asList(roleRepository.findByName(Constants.RoleName.ROLE_CUSTOMER.toString())));
    }
}
