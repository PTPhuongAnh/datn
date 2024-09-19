package com.graduates.test.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.LocalDateTime;
@Entity
@Table(name = "publisher")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer idPublisher;
private String namePublisher;
private String addressPublisher;
private LocalDateTime createAt;
private LocalDateTime updateAt;

//    public Publisher(int idPublisher, String namePublisher, String addressPublisher) {
//        this.idPublisher = idPublisher;
//        this.namePublisher = namePublisher;
//        this.addressPublisher = addressPublisher;
//    }


    public Publisher(Integer idPublisher, String namePublisher, String addressPublisher) {
        this.idPublisher = idPublisher;
        this.namePublisher = namePublisher;
        this.addressPublisher = addressPublisher;
    }

    public Publisher() {
    }

    public Publisher(Integer idPublisher) {
        this.idPublisher = idPublisher;
    }

    public int getIdPublisher() {
        return idPublisher;
    }

    public void setIdPublisher(int idPublisher) {
        this.idPublisher = idPublisher;
    }

    public String getNamePublisher() {
        return namePublisher;
    }

    public void setNamePublisher(String namePublisher) {
        this.namePublisher = namePublisher;
    }

    public String getAddressPublisher() {
        return addressPublisher;
    }

    public void setAddressPublisher(String addressPublisher) {
        this.addressPublisher = addressPublisher;
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
