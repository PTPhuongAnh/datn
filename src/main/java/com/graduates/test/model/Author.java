package com.graduates.test.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "author")
public class Author {
    @Id
    private int idAuthor;
    private String nameAuthor;
    private String addressAuthor;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public Author() {
    }

    public Author( String nameAuthor, String addressAuthor) {

        this.nameAuthor = nameAuthor;
        this.addressAuthor = addressAuthor;
    }

    public int getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
    }

    public String getAddressAuthor() {
        return addressAuthor;
    }

    public void setAddressAuthor(String addressAuthor) {
        this.addressAuthor = addressAuthor;
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
