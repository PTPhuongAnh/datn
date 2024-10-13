package com.graduates.test.resposity;

import com.graduates.test.model.Cart;
import com.graduates.test.model.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

 // List<Cart> findByUser_IdUser(Integer idUser);

  Optional<Cart> findByUserIdUser(Integer userId);

 //   List<Cart> findByUserIdUserAndCartDetailsIsDeletedTrueOrCartDetailsIsPurchasedTrue(Integer userId);
}
