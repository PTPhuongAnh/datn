package com.graduates.test.resposity;

import com.graduates.test.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {

  //  List<Feedback> findByOrderDetailBookIdBook(Integer bookId);
  List<Feedback> findByOrderDetailBookIdBook(Integer bookId);
 // List<Feedback> findByOrderDetail_Book_IdBookAndUser_IdBook(Integer bookId, Integer userId);

 // List<Feedback> findByBook_IdBookAndUser_IdUser(Integer bookId, Integer userId);

  List<Feedback> findByOrderDetail_Book_IdBook(Integer bookId);
  //  List<Feedback> findByOrderDetail_Book_IdBook(Integer bookId);
}
