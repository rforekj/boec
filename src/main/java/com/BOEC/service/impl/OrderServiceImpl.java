package com.BOEC.service.impl;

import com.BOEC.model.processing.cart.Cart;
import com.BOEC.model.processing.order.Order;
import com.BOEC.model.processing.order.OrderProduct;
import com.BOEC.model.processing.shipment.Shipment;
import com.BOEC.model.user.User;
import com.BOEC.repository.*;
import com.BOEC.service.OrderService;
import com.BOEC.service.dto.OrderCreateDto;
import com.BOEC.service.dto.OrderRespondDto;
import com.BOEC.service.dto.ShipmentRespondDto;
import com.BOEC.service.exception.CartIsBlankException;
import com.BOEC.service.exception.NotFoundIdException;
import com.BOEC.service.factory.ShipmentFactory;
import com.BOEC.service.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    ShipmentRepository shipmentRepository;
    @Autowired
    ShipmentFactory shipmentFactory;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartProductRepository cartProductRepository;
    @Autowired
    OrderProductRepository orderProductRepository;

    @Override
    public List<ShipmentRespondDto> getListShipment() {
        return shipmentFactory.shipmentsToShipmentRespondDtos((List<Shipment>) shipmentRepository.findAll());
    }

    @Override
    public OrderRespondDto createOrder(OrderCreateDto orderCreateDto) throws NotFoundIdException, CartIsBlankException {
        Order order = orderMapper.orderCreateDtoToOrder(orderCreateDto);
        order = orderRepository.save(order);
        for(OrderProduct orderProduct : order.getOrderProducts()) {
            orderProduct.setOrder(order);
        }
        orderProductRepository.saveAll(order.getOrderProducts());
        Cart cart = cartRepository.findByCustomer_Id(userRepository.findByEmail(order.getCreatedBy()).get().getId());
        if(cart!=null) {
            if(cart.getCartProducts().size()==0) throw new CartIsBlankException();
            cartProductRepository.deleteByCart_Id(cart.getId());
        }
        return orderMapper.orderToOrderRespondDto(order);
    }

    @Override
    public List<OrderRespondDto> listOrderOfCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        List<Order> orderList = orderRepository.findByCustomer_IdOrderByCreatedDateDesc(userRepository.findByEmail(username).get().getId());
        return orderMapper.ordersToOrderRespondDtos(orderList);
    }
}
