package com.graduates.test.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackRespone {

    private Integer idFeedback;
    private Integer userId;
    private String username;
    private Integer orderDetailId;
    private Integer bookId;
    private String bookTitle;
    private String comment;
    private Integer rating;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    public FeedbackRespone() {
    }

    public FeedbackRespone(Integer idFeedback, Integer userId, String username, Integer orderDetailId, Integer bookId, String bookTitle, String comment, Integer rating, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.idFeedback = idFeedback;
        this.userId = userId;
        this.username = username;
        this.orderDetailId = orderDetailId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.comment = comment;
        this.rating = rating;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    // Getters and Setters
    public Integer getIdFeedback() {
        return idFeedback;
    }

    public void setIdFeedback(Integer idFeedback) {
        this.idFeedback = idFeedback;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
