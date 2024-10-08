package com.graduates.test.resposity;

import com.graduates.test.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRespository extends JpaRepository<OrderStatus,Integer> {
}
