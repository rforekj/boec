package com.BOEC.model.user;

import com.BOEC.config.Constants;
import com.BOEC.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class BusinessEmployee extends Employee {
    @Autowired
    RoleRepository roleRepository;

    public BusinessEmployee() {
        super();
        this.setRoles(Arrays.asList(roleRepository.findByName(Constants.RoleName.ROLE_BUSINESS_EMPLOYEE.toString())));
    }
}
