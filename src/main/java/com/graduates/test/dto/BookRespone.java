package com.graduates.test.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.graduates.test.model.Feedback;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;


import java.time.LocalDateTime;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookRespone {
    private Integer idBook;

    private String nameBook;
    private String author;
    private String description_short;
    private String description_long;
    private String size;
    private String year_publisher;
    private String page_number;
    private String barcode;
    private String categoryName;
    private String publisherName;
    private String distributorName;
    private Integer quantity;
    private Integer price;
    private Integer categoryId;
    private Integer publisherId;
    private Integer distributorId;

    private List<String> imageUrls;

    private List<FeedbackRespone> feedbacks;

    public BookRespone() {
    }

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public BookRespone(Integer idBook, String nameBook, String author, Integer quantity, Integer price, List<String> imageUrls) {
        this.idBook = idBook;
        this.nameBook = nameBook;
        this.author = author;
        this.quantity = quantity;
        this.price = price;
        this.imageUrls = imageUrls;
    }

    public BookRespone(Integer idBook, String nameBook, String author, String description_short, String description_long, String size, String year_publisher, String page_number, String barcode, String categoryName, String publisherName, String distributorName, Integer quantity, Integer price, Integer categoryId, Integer publisherId, Integer distributorId, List<String> imageUrls, List<FeedbackRespone> feedbacks, LocalDateTime createAt, LocalDateTime updateAt) {
        this.idBook = idBook;
        this.nameBook = nameBook;
        this.author = author;
        this.description_short = description_short;
        this.description_long = description_long;
        this.size = size;
        this.year_publisher = year_publisher;
        this.page_number = page_number;
        this.barcode = barcode;
        this.categoryName = categoryName;
        this.publisherName = publisherName;
        this.distributorName = distributorName;
        this.quantity = quantity;
        this.price = price;
        this.categoryId = categoryId;
        this.publisherId = publisherId;
        this.distributorId = distributorId;
        this.imageUrls = imageUrls;
        this.feedbacks = feedbacks;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    public Integer getIdBook() {
        return idBook;
    }

    public void setIdBook(Integer idBook) {
        this.idBook = idBook;
    }

    public String getNameBook() {
        return nameBook;
    }

    public void setNameBook(String nameBook) {
        this.nameBook = nameBook;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription_short() {
        return description_short;
    }

    public void setDescription_short(String description_short) {
        this.description_short = description_short;
    }

    public String getDescription_long() {
        return description_long;
    }

    public void setDescription_long(String description_long) {
        this.description_long = description_long;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getYear_publisher() {
        return year_publisher;
    }

    public void setYear_publisher(String year_publisher) {
        this.year_publisher = year_publisher;
    }

    public String getPage_number() {
        return page_number;
    }

    public void setPage_number(String page_number) {
        this.page_number = page_number;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
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

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public List<FeedbackRespone> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackRespone> feedbacks) {
        this.feedbacks = feedbacks;
    }
}

