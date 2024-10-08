package com.graduates.test.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "order_status")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idStatus;

    private String status;

    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "orderStatus", cascade = CascadeType.ALL)
    private Order order;



    public OrderStatus() {}

    public OrderStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public OrderStatus(Integer idStatus, String status, Order order) {
        this.idStatus = idStatus;
        this.status = status;

        this.order = order;
    }

    public Integer getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Integer idStatus) {
        this.idStatus = idStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
