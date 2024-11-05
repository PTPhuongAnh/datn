package com.graduates.test.dto;

import java.util.List;

public class OrderDetailResponse {
    private Integer idOrderDetail;
    private List<FeedbackRespone> feedbacks;

    // Getter và Setter

    public Integer getIdOrderDetail() {
        return idOrderDetail;
    }

    public void setIdOrderDetail(Integer idOrderDetail) {
        this.idOrderDetail = idOrderDetail;
    }

    public List<FeedbackRespone> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackRespone> feedbacks) {
        this.feedbacks = feedbacks;
    }

}
