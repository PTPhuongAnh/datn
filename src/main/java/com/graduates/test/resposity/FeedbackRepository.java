package com.graduates.test.resposity;

import com.graduates.test.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {

  //  List<Feedback> findByOrderDetailBookIdBook(Integer bookId);
  List<Feedback> findByOrderDetailBookIdBook(Integer bookId);
}
