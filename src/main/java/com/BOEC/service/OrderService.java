package com.BOEC.service;

import com.BOEC.model.processing.shipment.Shipment;
import com.BOEC.service.dto.OrderCreateDto;
import com.BOEC.service.dto.OrderRespondDto;
import com.BOEC.service.dto.ShipmentRespondDto;
import com.BOEC.service.exception.CartIsBlankException;
import com.BOEC.service.exception.NotFoundIdException;

import java.util.List;

public interface OrderService{

    List<ShipmentRespondDto> getListShipment();

    OrderRespondDto createOrder(OrderCreateDto order) throws NotFoundIdException, CartIsBlankException;

    List<OrderRespondDto> listOrderOfCurrentUser();
}
