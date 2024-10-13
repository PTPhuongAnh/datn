package com.graduates.test.dto;

import com.graduates.test.model.Book;
import com.graduates.test.model.CartDetail;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {
    private Integer bookId; // ID của sách
    private String title; // Tiêu đề sách
    private String author; // Tác giả sách
    private String description; // Mô tả sách
    private int quantity; // Số lượng sách trong giỏ
    private double price; // Giá sách
    private List<String> imageUrls; // Danh sách URL ảnh của sách
    private double total;


    // Constructor

    public CartResponse() {
    }

    public CartResponse(Integer bookId, String title, String author, String description, int quantity, double price, List<String> imageUrls, double total) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.imageUrls = imageUrls;
        this.total = total;

    }

//    private CartResponse convertToCartResponse(CartDetail cartDetail) {
//        Book book = cartDetail.getBook();
//        List<String> imageUrls = getImageUrlsFromBook(book);
//        //  Cart cart=cartDetail.getCart();
//
//        return new CartResponse(
//                book.getIdBook(),
//                book.getNameBook(),
//                book.getAuthor(),
//                book.getDescription_short(),
//                cartDetail.getQuantity(),
//                cartDetail.getPrice(),
//                imageUrls,
//                book.getPrice() * cartDetail.getQuantity()
//
//
//        );
//    }
//
//    private List<String> getImageUrlsFromBook(Book book) {
//        String baseUrl = "http://localhost:8080/book/image/";
//        return book.getImageBooks().stream()
//                .map(image -> baseUrl + encodeURIComponent(image.getImage_url()))
//                .collect(Collectors.toList());
//    }
//
//    private String encodeURIComponent(String value) {
//        try {
//            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//    }

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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }


}
