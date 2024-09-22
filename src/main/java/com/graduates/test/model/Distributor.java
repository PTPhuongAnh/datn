package com.graduates.test.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "distributor")
public class Distributor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDistributor;

    private String nameDistributor;
    private String address;
    private String phone;
    private String email;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    public Distributor(Integer idDistributor, String nameDistributor, String address, String phone, String email) {
        this.idDistributor = idDistributor;
        this.nameDistributor = nameDistributor;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public Distributor() {
    }

    public Distributor(Integer idDistributor) {
        this.idDistributor = idDistributor;
    }

    public Integer getIdDistributor() {
        return idDistributor;
    }

    public void setIdDistributor(Integer idDistributor) {
        this.idDistributor = idDistributor;
    }

    public String getNameDistributor() {
        return nameDistributor;
    }

    public void setNameDistributor(String nameDistributor) {
        this.nameDistributor = nameDistributor;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }
}
