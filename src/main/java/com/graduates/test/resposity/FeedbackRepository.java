package com.graduates.test.resposity;

import com.graduates.test.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {


  List<Feedback> findByOrderDetailBookIdBook(Integer bookId);


  List<Feedback> findByOrderDetail_Book_IdBookOrderByCreatedAtDesc(Integer bookId);
  //  List<Feedback> findByOrderDetail_Book_IdBook(Integer bookId);

//  List<Feedback> findByOrderDetailBookIdAndIsVisibleTrue(Integer bookId);
//
//  // Truy vấn để thay đổi trạng thái hiển thị của bình luận
//  @Modifying
//  @Query("UPDATE Feedback f SET f.isVisible = :isVisible WHERE f.idFeedback = :idFeedback")
//  void updateFeedbackVisibility(@Param("idFeedback") Integer idFeedback, @Param("isVisible") Boolean isVisible);
}
