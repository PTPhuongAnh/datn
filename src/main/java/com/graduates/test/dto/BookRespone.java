package com.graduates.test.dto;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;
import java.util.List;

public class BookRespone {
    private Integer idBook;
    private String nameBook;
    private String author;
    private String description;
    private String categoryName;
    private String publisherName;
    private Integer quantity;
    private Integer price;

    private List<String> imageUrls;


    public BookRespone() {
    }

//    public BookRespone(int idBook, String nameBook, String author, String description, String categoryName, String publisherName, List<String> imageUrls) {
//        this.idBook = idBook;
//        this.nameBook = nameBook;
//        this.author = author;
//        this.description = description;
//        this.categoryName = categoryName;
//        this.publisherName = publisherName;
//        this.imageUrls = imageUrls;
//
//    }

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    public BookRespone(Integer idBook, String nameBook, String author, String description, String categoryName, String publisherName, Integer quantity, Integer price, List<String> imageUrls) {
        this.idBook = idBook;
        this.nameBook = nameBook;
        this.author = author;
        this.description = description;
        this.categoryName = categoryName;
        this.publisherName = publisherName;
        this.quantity = quantity;
        this.price = price;
        this.imageUrls = imageUrls;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
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
    public LocalDateTime getCreateAt() {
        return createAt;
    }



    public LocalDateTime getUpdateAt() {
        return updateAt;
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

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
    //    public static class BookUpdate {
//        private String nameBook;
//        private String author;
//        private String description;
//        private Integer categoryId;
//        private Integer publisherId;
//        private List<String> imageUrls;
//
//        public String getNameBook() {
//            return nameBook;
//        }
//
//        public void setNameBook(String nameBook) {
//            this.nameBook = nameBook;
//        }
//
//        public String getAuthor() {
//            return author;
//        }
//
//        public void setAuthor(String author) {
//            this.author = author;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public void setDescription(String description) {
//            this.description = description;
//        }
//
//        public Integer getCategoryId() {
//            return categoryId;
//        }
//
//        public void setCategoryId(int categoryId) {
//            this.categoryId = categoryId;
//        }
//
//        public Integer  getPublisherId() {
//            return publisherId;
//        }
//
//        public void setPublisherId(int publisherId) {
//            this.publisherId = publisherId;
//        }
//
//        public List<String> getImageUrls() {
//            return imageUrls;
//        }
//
//        public void setImageUrls(List<String> imageUrls) {
//            this.imageUrls = imageUrls;
//        }
//    }
}
