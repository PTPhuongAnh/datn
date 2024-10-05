package com.graduates.test.resposity;

import com.graduates.test.model.Cart;
import com.graduates.test.model.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {
    List<CartDetail> findByCart_User_IdUser(Integer idUser);
}
