package com.graduates.test.resposity;

import com.graduates.test.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRespository extends JpaRepository<Shipment,Integer> {
}
