package com.graduates.test.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private Integer idTransaction;

    @ManyToOne
    @JoinColumn(name = "id_order", referencedColumnName = "id_order")
    private Order order; // Liên kết với đơn hàng

    @ManyToOne
    @JoinColumn(name = "id_payment", referencedColumnName = "id_payment")
    private Payment paymentMethod; // Phương thức thanh toán (Momo, Thẻ tín dụng, v.v.)

    private Double amount; // Số tiền giao dịch
    private String paymentStatus; // Trạng thái thanh toán (VD: Thành công, Thất bại, Đang chờ xử lý)
    private String momoTransactionId; // Mã giao dịch từ Momo
    private String transactionDate; // Ngày giao dịch

    public Transaction() {}

    public Transaction(Order order, Payment paymentMethod, Double amount, String paymentStatus, String momoTransactionId, String transactionDate) {
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.momoTransactionId = momoTransactionId;
        this.transactionDate = transactionDate;
    }

    public Integer getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(Integer idTransaction) {
        this.idTransaction = idTransaction;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Payment getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Payment paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getMomoTransactionId() {
        return momoTransactionId;
    }

    public void setMomoTransactionId(String momoTransactionId) {
        this.momoTransactionId = momoTransactionId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
}
