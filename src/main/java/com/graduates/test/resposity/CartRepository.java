package com.graduates.test.resposity;

import com.graduates.test.model.Cart;
import com.graduates.test.model.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
   // List<CartDetail> findByUserId(Integer userId);
  // List<Cart> findByUser_idUser(Integer userId);

   // List<CartDetail> getCartByUserId(Integer userId);

   // List<CartDetail> findByUserId(Integer userId);

 //   List<CartDetail> findByCart_UserId(Integer userId);

  //  List<CartDetail> findByCart_User_IdUser(Integer userId);

  //  List<Cart> findByUser_Id(Integer userId);
  List<Cart> findByUser_IdUser(Integer idUser);
}
