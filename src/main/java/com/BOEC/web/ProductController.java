package com.BOEC.web;

import com.BOEC.service.CartService;
import com.BOEC.service.ProductService;
import com.BOEC.service.dto.ProductCreateDto;
import com.BOEC.service.exception.ItemIsPublicException;
import com.BOEC.service.exception.NotFoundIdException;
import com.BOEC.service.exception.OutOfStockException;
import com.BOEC.service.exception.ProductNotInCartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SALE_EMPLOYEE') or hasRole('ROLE_BUSINESS_EMPLOYEE') or hasRole('ROLE_WAREHOUSE_EMPLOYEE')")
    @GetMapping
    Map<String, Object> listProduct(@RequestParam(required = false) String keyword, int page, int size) {
        return productService.listProduct(keyword, page, size);
    }

    @PreAuthorize("hasRole('ROLE_BUSINESS_EMPLOYEE')")
    @PostMapping
    ResponseEntity<?> saveProduct(@RequestBody ProductCreateDto product) {
        try {
            int id = productService.saveProduct(product);
            return ResponseEntity.status(201).body(id);
        } catch (NotFoundIdException e) {
            return ResponseEntity.status(400).body("not found id item");
        } catch (ItemIsPublicException e) {
            return ResponseEntity.status(400).body("item is public");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("error");
        }
    }

    @PreAuthorize("hasRole('ROLE_BUSINESS_EMPLOYEE')")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteProduct(@PathVariable int id) {
        try {
            return ResponseEntity.status(201).body(productService.deleteProduct(id));
        } catch (NotFoundIdException e) {
            return ResponseEntity.status(400).body("not found id product");
        }
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("{id}/add-to-cart")
    ResponseEntity<?> addToCart(@PathVariable int id) {
        try {
            cartService.addProduct(id);
            return ResponseEntity.ok("succeed");
        } catch (NotFoundIdException e) {
            return ResponseEntity.status(400).body("not found id cart or product");
        } catch (OutOfStockException e) {
            return ResponseEntity.status(400).body("product is out of stock");
        }
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @DeleteMapping("{id}/remove-from-cart")
    ResponseEntity<?> removeFromCart(@PathVariable int id) {
        try {
            cartService.removeProduct(id);
            return ResponseEntity.ok("succeed");
        } catch (NotFoundIdException e) {
            return ResponseEntity.status(400).body("not found id cart or product");
        } catch (ProductNotInCartException e) {
            return ResponseEntity.status(400).body("product is not in cart");
        }
    }
}
