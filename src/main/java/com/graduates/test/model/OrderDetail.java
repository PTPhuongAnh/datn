package com.graduates.test.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_detail")
    private Integer idOrderDetail;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id_order")
    private Order order; // Liên kết với Order

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id_book")
    private Book book; // Sản phẩm sách trong đơn hàng

    private int quantity; // Số lượng sách trong đơn hàng

    private double price; // Giá của sản phẩm

    public OrderDetail() {}

    public OrderDetail(Order order, Book book, int quantity, double price) {
        this.order = order;
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getIdOrderDetail() {
        return idOrderDetail;
    }

    public void setIdOrderDetail(Integer idOrderDetail) {
        this.idOrderDetail = idOrderDetail;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
