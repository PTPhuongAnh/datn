package com.graduates.test.resposity;

import com.graduates.test.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {



  List<Feedback> findByOrderDetail_Order_IdAndOrderDetail_Book_IdBookAndOrderDetail_Order_User_Username(Integer orderId, Integer bookId, String username);

  List<Feedback> findByOrderDetail_Book_IdBookOrderByCreatedAtDesc(Integer bookId);

    List<Feedback> findAllByOrderByCreatedAtDesc();



    @Query("UPDATE Feedback f SET f.isVisible = :isVisible WHERE f.id = :id")
    void updateVisibility(@Param("id") Integer id, @Param("isVisible") Boolean isVisible);


    Page<Feedback> findByUserUsernameContaining(String account, Pageable pageable);
}
