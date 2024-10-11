package com.graduates.test.resposity;

import com.graduates.test.model.Cart;
import com.graduates.test.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRespository extends JpaRepository<Order,Integer> {
    boolean existsByCart(Cart cart);

    List<Order> findByUser_IdUser(Integer userId);
}
