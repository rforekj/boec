package com.BOEC.repository;

import com.BOEC.model.processing.shipment.Shipment;
import org.springframework.data.repository.CrudRepository;

public interface ShipmentRepository extends CrudRepository<Shipment, Integer> {
}
