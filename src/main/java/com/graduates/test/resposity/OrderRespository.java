package com.graduates.test.resposity;

import com.graduates.test.model.Cart;
import com.graduates.test.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRespository extends JpaRepository<Order,Integer> {
    boolean existsByCart(Cart cart);
    List<Order> findByUser_idUser(Integer userId);
    Optional<Order> findByIdAndUser_idUser(Integer orderId, Integer userId);
    List<Order> findByUser_IdUserAndOrderStatus_IdStatus(Integer userId, Integer statusId);
}
