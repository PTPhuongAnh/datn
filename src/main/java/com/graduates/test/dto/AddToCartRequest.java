package com.graduates.test.dto;

public class AddToCartRequest {
    private Integer userId;   // ID của người dùng
    private Integer bookId;   // ID của sách
    private Integer quantity;  // Số lượng sách

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
