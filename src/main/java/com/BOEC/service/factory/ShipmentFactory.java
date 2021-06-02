package com.BOEC.service.factory;

import com.BOEC.model.processing.shipment.FastShip;
import com.BOEC.model.processing.shipment.NormalShip;
import com.BOEC.model.processing.shipment.Shipment;
import com.BOEC.service.dto.ShipmentRespondDto;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShipmentFactory {

    public ShipmentRespondDto shipmentToShipmentRespondDto(Shipment shipment) {
        ShipmentRespondDto shipmentRespondDto = new ShipmentRespondDto();
        shipmentRespondDto.setId(shipment.getId());
        shipmentRespondDto.setShippingCost(shipment.getShippingCost());
        shipmentRespondDto.setShippingCompany(shipment.getShippingCompany());
        shipmentRespondDto.setShippingTime(shipment.getShippingTime());
        if(shipment instanceof FastShip) {
            shipmentRespondDto.setType(ShipmentRespondDto.ShipmentType.FAST_SHIP);
        } else if(shipment instanceof NormalShip) {
            shipmentRespondDto.setType(ShipmentRespondDto.ShipmentType.NORMAL_SHIP);
        }
        return shipmentRespondDto;
    }

    public List<ShipmentRespondDto> shipmentsToShipmentRespondDtos(List<Shipment> shipments) {
        return shipments.stream().map(this::shipmentToShipmentRespondDto).collect(Collectors.toList());
    }
}
