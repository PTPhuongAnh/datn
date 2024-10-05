package com.graduates.test.dto;

import java.util.List;

public class CartResponse {
    private Integer bookId; // ID của sách
    private String title; // Tiêu đề sách
    private String author; // Tác giả sách
    private String description; // Mô tả sách
    private int quantity; // Số lượng sách trong giỏ
    private double price; // Giá sách
    private List<String> imageUrls; // Danh sách URL ảnh của sách

    // Constructor

    public CartResponse() {
    }

    public CartResponse(Integer bookId, String title, String author, String description, int quantity, double price, List<String> imageUrls) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.imageUrls = imageUrls;
    }

    // Getters and Setters
    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
