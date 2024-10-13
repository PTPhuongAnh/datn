package com.graduates.test.service;

public interface FeedbackService {
    String addFeedback(Integer userId, Integer orderDetailId, String comment, Integer rating);
}
