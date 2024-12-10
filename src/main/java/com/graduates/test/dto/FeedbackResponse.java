package com.graduates.test.dto;

import java.time.LocalDateTime;

public class FeedbackResponse {
    private Integer id;
    private String username;     // Tên người dùng đã đánh giá
    private String bookTitle;    // Tên sách đã được đánh giá
    private String comment;      // Nội dung đánh giá
    private Integer rating;      // Điểm đánh giá
    private LocalDateTime createdAt; // Thời gian tạo đánh giá
    private Boolean isVisible;

    public FeedbackResponse() {
    }

    public FeedbackResponse(Integer id, String username, String bookTitle, String comment, Integer rating, LocalDateTime createdAt, Boolean isVisible) {
        this.id = id;
        this.username = username;
        this.bookTitle = bookTitle;
        this.comment = comment;
        this.rating = rating;
        this.createdAt = createdAt;
        this.isVisible = isVisible;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Getter và Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
