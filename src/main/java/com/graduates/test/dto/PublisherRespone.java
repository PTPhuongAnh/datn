package com.graduates.test.dto;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class PublisherRespone {
    private Integer idPublisher;
    private String namePublisher;
    private String addressPublisher;
    private String phonePublisher;
    private String emailPublisher;
private String publisherCode;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
  //  private boolean deleted=false;
    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    public PublisherRespone() {
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

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getPublisherCode() {
        return publisherCode;
    }

    public void setPublisherCode(String publisherCode) {
        this.publisherCode = publisherCode;
    }
}
