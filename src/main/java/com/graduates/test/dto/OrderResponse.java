package com.graduates.test.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    private Integer id;
    private String orderCode;
    private Integer bookId; // ID của sách
    private String title; // Tiêu đề sách
    private String author; // Tác giả sách
    private String description; // Mô tả sách
    private int quantity; // Số lượng sách trong giỏ
    private double price; // Giá sách
    private List<String> imageUrls; // Danh sách URL ảnh của sách
    private double total;
    private String shipment;
    private String payment;
    private String phone;
    private String shippingAdrress;
    private String receiveName;
    private LocalDateTime date;
    private String status;
    private String note;
    private LocalDateTime deliveryDate;
    private List<BookRespone> books = new ArrayList<>();
    private LocalDateTime createdAt;
    private List<FeedbackRespone> feedbacks;
    public OrderResponse() {
    }

    public OrderResponse(Integer id, String orderCode, Integer bookId, String title, String author, String description, int quantity, double price, List<String> imageUrls, double total, String shipment, String payment, String phone, String shippingAdrress, String receiveName, LocalDateTime date, String status, String note, LocalDateTime deliveryDate, List<BookRespone> books, LocalDateTime createdAt, List<FeedbackRespone> feedbacks) {
        this.id = id;
        this.orderCode = orderCode;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.imageUrls = imageUrls;
        this.total = total;
        this.shipment = shipment;
        this.payment = payment;
        this.phone = phone;
        this.shippingAdrress = shippingAdrress;
        this.receiveName = receiveName;
        this.date = date;
        this.status = status;
        this.note = note;
        this.deliveryDate = deliveryDate;
        this.books = books;
        this.createdAt = createdAt;
        this.feedbacks = feedbacks;
    }

    public List<BookRespone> getBooks() {
        return books;
    }

    public void setBooks(List<BookRespone> books) {
        this.books = books;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShippingAdrress() {
        return shippingAdrress;
    }

    public void setShippingAdrress(String shippingAdrress) {
        this.shippingAdrress = shippingAdrress;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public List<FeedbackRespone> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackRespone> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
