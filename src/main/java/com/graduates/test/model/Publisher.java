package com.graduates.test.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "publisher")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer idPublisher;
private String namePublisher;
private String addressPublisher;
    private String phonePublisher;
    private String emailPublisher;

private LocalDateTime createAt;
private LocalDateTime updateAt;


    public Publisher(Integer idPublisher, String namePublisher, String addressPublisher, String phonePublisher, String emailPublisher) {
        this.idPublisher = idPublisher;
        this.namePublisher = namePublisher;
        this.addressPublisher = addressPublisher;
        this.phonePublisher = phonePublisher;
        this.emailPublisher = emailPublisher;
    }

    public Publisher() {
    }

    public Publisher(Integer idPublisher) {
        this.idPublisher = idPublisher;
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

    public Integer getIdPublisher() {
        return idPublisher;
    }

    public void setIdPublisher(Integer idPublisher) {
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

    public String getPhonePublisher() {
        return phonePublisher;
    }

    public void setPhonePublisher(String phonePublisher) {
        this.phonePublisher = phonePublisher;
    }

    public String getEmailPublisher() {
        return emailPublisher;
    }

    public void setEmailPublisher(String emailPublisher) {
        this.emailPublisher = emailPublisher;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }


}
