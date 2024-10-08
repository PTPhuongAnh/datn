package com.graduates.test.resposity;

import com.graduates.test.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRespository extends JpaRepository<Order,Integer> {
}
