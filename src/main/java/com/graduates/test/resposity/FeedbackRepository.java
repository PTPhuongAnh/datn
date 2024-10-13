package com.graduates.test.resposity;

import com.graduates.test.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {

}
