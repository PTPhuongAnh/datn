package com.graduates.test.service;

import com.graduates.test.dto.FeedbackRespone;
import com.graduates.test.model.Feedback;

import java.util.List;

public interface FeedbackService {
    List<FeedbackRespone> getFeedbackResponsesByBookId(Integer bookId);
    String addFeedback(Integer userId, Integer orderDetailId, Integer bookId,String comment, Integer rating);
}
