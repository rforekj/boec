package com.BOEC.web;

import com.BOEC.service.UserService;
import com.BOEC.service.dto.UserRespondDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list-user")
    public Map<String, Object> findAll(@RequestParam int page, @RequestParam int size) {
        return userService.findAll(page, size);
    }

    @GetMapping
    public UserRespondDto getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.ok(200);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Not found id user");
        }
    }
}
