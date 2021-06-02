package com.BOEC.model.user;

import com.BOEC.config.Constants;
import com.BOEC.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class WareHouseEmployee extends Employee {
    @Autowired
    RoleRepository roleRepository;

    public WareHouseEmployee() {
        super();
        this.setRoles(Arrays.asList(roleRepository.findByName(Constants.RoleName.ROLE_WAREHOUSE_EMPLOYEE.toString())));
    }
}
