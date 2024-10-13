package com.graduates.test.resposity;

import com.graduates.test.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRespository extends JpaRepository<OrderStatus,Integer> {
    OrderStatus findByStatus(String pending);


}
