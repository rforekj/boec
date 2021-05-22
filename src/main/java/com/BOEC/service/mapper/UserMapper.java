package com.BOEC.service.mapper;

import com.BOEC.config.Constants;
import com.BOEC.model.Role;
import com.BOEC.model.user.User;
import com.BOEC.repository.RoleRepository;
import com.BOEC.service.dto.UserRegistrationDto;
import com.BOEC.service.dto.UserRespondDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserRespondDto userToUserRespondDto(User user) {
        UserRespondDto userRespondDto = new UserRespondDto();
        userRespondDto.setAddress(user.getAddress());
        userRespondDto.setEmail(user.getEmail());
        userRespondDto.setPhone(user.getPhone());
        userRespondDto.setName(user.getName());
        userRespondDto.setRoles(user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()));
        userRespondDto.setId(user.getId());
        return userRespondDto;
    }

    public User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        List<String> currentUserRole = new ArrayList<>();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            currentUserRole = ((UserDetails) principal).getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList());
        } else {
            username = principal.toString();
        }
        User user = new User();
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setEmail(userRegistrationDto.getEmail());
        user.setName(userRegistrationDto.getName());
        user.setPhone(userRegistrationDto.getPhone());
        user.setAddress(userRegistrationDto.getAddress());
        user.setCreatedBy(username);
        List<Role> roleList = new ArrayList<>();
        for(String role:userRegistrationDto.getRoles()) {
            if(role.equals(Constants.RoleName.ROLE_CUSTOMER.name())) {
                roleList.add(roleRepository.findByName(role));
                break;
            } else if(role.equals(Constants.RoleName.ROLE_WAREHOUSE_EMPLOYEE.name())
                    || role.equals(Constants.RoleName.ROLE_BUSINESS_EMPLOYEE.name())
                    || role.equals(Constants.RoleName.ROLE_SALE_EMPLOYEE.name())
                    || role.equals(Constants.RoleName.ROLE_ADMIN.name())) {
                if(currentUserRole.contains(Constants.RoleName.ROLE_ADMIN.name())) {
                    roleList.add(roleRepository.findByName(role));
                    break;
                } else {
                    throw new SecurityException();
                }
            }

        }
        user.setRoles(roleList);
        return user;
    }

    public List<UserRespondDto> usersToUserRespondDtos(List<User> users) {
        return users.stream().map(this::userToUserRespondDto).collect(Collectors.toList());
    }
}
