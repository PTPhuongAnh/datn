package com.graduates.test.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "image_book")
public class ImageBook {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idImageBook;
    private String image_url;
    @ManyToOne
    @JoinColumn(name = "id_book", nullable = false)
    private Book book;  // Sách mà ảnh này thuộc về

    private LocalDateTime createAt;
    private LocalDateTime updateAt;


    public ImageBook(int idImageBook, String image_url) {
        this.idImageBook = idImageBook;
        this.image_url = image_url;

    }

    public ImageBook() {
    }

    public int getIdImageBook() {
        return idImageBook;
    }

    public void setIdImageBook(int idImageBook) {
        this.idImageBook = idImageBook;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }
}
