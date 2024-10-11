package com.graduates.test.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private Integer idPayment;

    private String paymentMethod; // Phương thức thanh toán (VD: Thẻ tín dụng, PayPal, COD)

//    @OneToOne(mappedBy = "payment")
//    private Order order; // Liên kết với Order

    public Payment() {}

    public Payment( String paymentMethod) {

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


}
