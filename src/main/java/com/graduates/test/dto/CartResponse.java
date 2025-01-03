package com.graduates.test.dto;

import com.graduates.test.model.Book;
import com.graduates.test.model.CartDetail;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {
    private Integer idCartDetail;
    private Integer bookId; // ID của sách
    private String title; // Tiêu đề sách
    private String author; // Tác giả sách
    private String description; // Mô tả sách
    private int quantity; // Số lượng sách trong giỏ
    private Integer price; // Giá sách
    private List<String> imageUrls; // Danh sách URL ảnh của sách
    private double total;
    private int cl;

    public Integer getIdCartDetail() {
        return idCartDetail;
    }

    public void setIdCartDetail(Integer idCartDetail) {
        this.idCartDetail = idCartDetail;
    }

    // Constructor

    public CartResponse() {
    }

    public CartResponse(Integer idCartDetail, Integer bookId, String title, String author, String description, int quantity, Integer price, List<String> imageUrls, double total, int cl) {
        this.idCartDetail = idCartDetail;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.imageUrls = imageUrls;
        this.total = total;
        this.cl = cl;
    }

    public int getCl() {
        return cl;
    }

    public void setCl(int cl) {
        this.cl = cl;
    }

    public Integer getIdCart() {
        return idCartDetail;
    }

    public void setIdCart(Integer idCartDetail) {
        this.idCartDetail = idCartDetail;
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

    public void setPrice(Integer price) {
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


}
