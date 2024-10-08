package com.graduates.test.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idPayment;

    private String paymentMethod; // Phương thức thanh toán (VD: Thẻ tín dụng, PayPal, COD)

    @OneToOne(mappedBy = "payment")
    private Order order; // Liên kết với Order

    public Payment() {}

    public Payment(Order order, String paymentMethod) {
        this.order = order;
        this.paymentMethod = paymentMethod;
    }

    public Integer getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(Integer idPayment) {
        this.idPayment = idPayment;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
