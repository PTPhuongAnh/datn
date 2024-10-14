package com.graduates.test.resposity;

import com.graduates.test.model.Book;
import com.graduates.test.model.Cart;
import com.graduates.test.model.CartDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {
    Optional<CartDetail> findByCartAndBook(Cart cart, Book book);
    Optional<CartDetail> findByBook_IdBook(Integer idBook);

    Optional<CartDetail> findByIdAndCart_IdCart(Integer detailId, Integer idCart);
   // Optional<CartDetail> findByCartInAndIsDeletedFalseAndIsPurchasedFalse(Optional<Cart> carts);
   List<CartDetail> findByCartAndIsDeletedFalseAndIsPurchasedFalse(Cart cart);
  //  Optional<CartDetail> findByCartAndIsDeletedFalseAndIsPurchasedFalse(Cart cart);
  Page<CartDetail> findByCartAndIsDeletedFalseAndIsPurchasedFalse(Cart cart, Pageable pageable);

    Optional<CartDetail> findByBook_IdBookAndCart_IdCart(Integer idBook, Integer idCart);
}
