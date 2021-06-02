package com.BOEC.web;

import com.BOEC.service.ItemService;
import com.BOEC.service.dto.ItemImportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @PreAuthorize("hasRole('ROLE_WAREHOUSE_EMPLOYEE') or hasRole('ROLE_BUSINESS_EMPLOYEE')")
    @GetMapping
    Map<String, Object> listItem(@RequestParam(required = false) String keyword, int page, int size) {
        return itemService.listItem(keyword, page, size);
    }

    @PreAuthorize("hasRole('ROLE_WAREHOUSE_EMPLOYEE')")
    @PostMapping
    ResponseEntity<?> saveItem(@ModelAttribute ItemImportDto item) {
        return ResponseEntity.status(201).body(itemService.saveItem(item));
    }

    @PreAuthorize("hasRole('ROLE_WAREHOUSE_EMPLOYEE')")
    @PutMapping("/{id}")
    ResponseEntity<?> saveItem(@PathVariable int id, @ModelAttribute ItemImportDto item) {
        if (itemService.findById(id) == null)
            return ResponseEntity.status(404).body("not found id item");
        item.setId(id);
        return ResponseEntity.status(201).body(itemService.saveItem(item));
    }

    @PreAuthorize("hasRole('ROLE_WAREHOUSE_EMPLOYEE')")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteItem(@PathVariable int id) {
        try {
            return ResponseEntity.status(201).body(itemService.deleteItem(id));
        } catch (Exception e) {
            return ResponseEntity.status(400).body("not found id item");
        }
    }

}
