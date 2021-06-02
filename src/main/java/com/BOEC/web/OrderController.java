package com.BOEC.web;

import com.BOEC.model.processing.shipment.Shipment;
import com.BOEC.service.OrderService;
import com.BOEC.service.dto.OrderCreateDto;
import com.BOEC.service.dto.OrderRespondDto;
import com.BOEC.service.dto.ShipmentRespondDto;
import com.BOEC.service.exception.CartIsBlankException;
import com.BOEC.service.exception.NotFoundIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/shipment")
    public List<ShipmentRespondDto> getListShipment(){
        return orderService.getListShipment();
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateDto orderCreateDto){
        try {
            return ResponseEntity.ok().body(orderService.createOrder(orderCreateDto));
        } catch (NotFoundIdException e) {
            return ResponseEntity.status(400).body("Not found ID");
        } catch (CartIsBlankException e) {
            return ResponseEntity.status(400).body("cart is blank");
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public List<OrderRespondDto> listOrder() {
        return orderService.listOrderOfCurrentUser();
    }
}
