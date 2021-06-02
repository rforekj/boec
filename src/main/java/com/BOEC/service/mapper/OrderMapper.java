package com.BOEC.service.mapper;

import com.BOEC.model.processing.cart.CartProduct;
import com.BOEC.model.processing.order.Order;
import com.BOEC.model.processing.shipment.Shipment;
import com.BOEC.model.user.User;
import com.BOEC.repository.CartRepository;
import com.BOEC.repository.ShipmentRepository;
import com.BOEC.repository.UserRepository;
import com.BOEC.service.dto.*;
import com.BOEC.service.exception.NotFoundIdException;
import com.BOEC.service.factory.ShipmentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderMapper {

    @Autowired
    ShipmentRepository shipmentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ShipmentFactory shipmentFactory;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartProductMapper cartProductMapper;
    @Autowired
    OrderProductMapper orderProductMapper;


    public Order orderCreateDtoToOrder(OrderCreateDto orderCreateDto) throws NotFoundIdException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        Order order = new Order();
        order.setCreatedBy(username);
        Optional<Shipment> shipment = shipmentRepository.findById(orderCreateDto.getShipmentId());
        if(shipment.isPresent())
            order.setShipment(shipment.get());
        else throw new NotFoundIdException();
        order.setPhone(orderCreateDto.getPhone());
        order.setStatus(Order.OrderStatus.WAIT_FOR_APPROVE);
        order.setShippingAddress(orderCreateDto.getShippingAddress());
        double total = 0;
        CustomerRespondDto user = (CustomerRespondDto) userMapper.userToUserRespondDto(userRepository.findByEmail(username).get());
        User user1 = new User();
        user1.setId(user.getId());
        order.setCustomer(user1);
        for (CartProductRespondDto cartProduct:user.getCart().getCartProducts()){
            total += cartProduct.getProduct().getPrice()*cartProduct.getQuantity();
        }
        order.setOrderProducts(cartProductMapper.cartProductsToOrderProducts(cartRepository.findByCustomer_Id(user.getId()).getCartProducts()));
        order.setTotal(total+shipment.get().getShippingCost());
        return order;
    }

    public OrderRespondDto orderToOrderRespondDto(Order order) throws NotFoundIdException {
        OrderRespondDto orderRespondDto = new OrderRespondDto();
        orderRespondDto.setId(order.getId());
        orderRespondDto.setCreatedDate(Date.from(order.getCreatedDate()));
        Optional<User> user = userRepository.findByEmail(order.getCreatedBy());
        if(user.isPresent()) {
            orderRespondDto.setCustomerName(user.get().getName());
        } else {
            throw new NotFoundIdException();
        }
        orderRespondDto.setStatus(order.getStatus());
        orderRespondDto.setTotal(order.getTotal());
        orderRespondDto.setPhone(order.getPhone());
        orderRespondDto.setShipment(shipmentFactory.shipmentToShipmentRespondDto(order.getShipment()));
        orderRespondDto.setShippingAddress(order.getShippingAddress());
        orderRespondDto.setOrderProducts(orderProductMapper.orderProductsToOrderProductDtos(order.getOrderProducts()));
        return orderRespondDto;
    }

    public List<OrderRespondDto> ordersToOrderRespondDtos(List<Order> orderList) {
            return orderList.stream().map(a -> {
                try {
                    return orderToOrderRespondDto(a);
                } catch (NotFoundIdException e) {
                    return null;
                }
            }).collect(Collectors.toList());
    }
}
