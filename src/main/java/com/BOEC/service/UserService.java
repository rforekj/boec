package com.BOEC.service;

import com.BOEC.service.dto.UserRegistrationDto;
import com.BOEC.service.dto.UserRespondDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface UserService extends UserDetailsService {
    UserRespondDto findByEmail(String email);

    UserRespondDto save(UserRegistrationDto registration);

    Map<String, Object> findAll(int page, int size);

    UserRespondDto getCurrentUser();

    void deleteById(Long id);
}
