package com.graduates.test.service;

import com.graduates.test.dto.FeedbackRespone;
import com.graduates.test.model.Feedback;

import java.util.List;

public interface FeedbackService {

    String updateFeedbacks(String token, List<FeedbackRespone> feedbackUpdates) throws Exception;
  String addFeedbacks(String token, Integer orderId, List<FeedbackRespone> feedbackDTOs) throws Exception;

}
