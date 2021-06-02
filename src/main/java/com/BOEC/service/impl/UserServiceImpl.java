package com.BOEC.service.impl;

import com.BOEC.config.Constants;
import com.BOEC.model.Role;
import com.BOEC.model.processing.cart.Cart;
import com.BOEC.model.user.User;
import com.BOEC.repository.CartRepository;
import com.BOEC.repository.UserRepository;
import com.BOEC.service.UserService;
import com.BOEC.service.dto.UserRegistrationDto;
import com.BOEC.service.dto.UserRespondDto;
import com.BOEC.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CartRepository cartRepository;

    public UserRespondDto findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) return userMapper.userToUserRespondDto(user.get());
        return null;
    }

    public UserRespondDto save(UserRegistrationDto registration) {
        User user = userRepository.save(userMapper.userRegistrationDtoToUser(registration));
        if (registration.getRoles().contains(Constants.RoleName.ROLE_CUSTOMER.toString())) {
            Cart cart = new Cart();
            cart.setCustomer(user);
            cartRepository.save(cart);
        }
        return userMapper.userToUserRespondDto(user);
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(),
                user.get().getPassword(),
                mapRolesToAuthorities(user.get().getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    public Map<String, Object> findAll(int page, int size) {
        if (page < 1 || size < 1) return null;
        Pageable paging = PageRequest.of(page - 1, size, Sort.by("id"));
        Page<User> pageTuts = userRepository.findAll(paging);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", pageTuts.getNumber() + 1);
        response.put("totalItems", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());
        response.put("listUser", userMapper.usersToUserRespondDtos(pageTuts.getContent()));
        return response;
    }

    @Override
    public UserRespondDto getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            Optional<User> user = userRepository.findByEmail(((UserDetails) principal).getUsername());
            if (user.isPresent()) return userMapper.userToUserRespondDto(user.get());
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
