package com.graduates.test.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "payment_statusm")
public class PaymentStatusM {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "status_name", unique = true, nullable = false)
    private String statusName;

    public PaymentStatusM() {}

    public PaymentStatusM(String statusName) {
        this.statusName = statusName;
    }

    public PaymentStatusM(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
