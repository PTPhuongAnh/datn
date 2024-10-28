package com.graduates.test.service;

import com.graduates.test.dto.FeedbackRespone;
import com.graduates.test.model.Feedback;

import java.util.List;

public interface FeedbackService {
    String addFeedbacks(Integer userId, List<FeedbackRespone> feedbackDTOs);

    String updateFeedbacks(Integer userId, List<FeedbackRespone> feedbackUpdates);

}
