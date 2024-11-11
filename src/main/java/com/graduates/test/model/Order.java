package com.graduates.test.model;

import ch.qos.logback.core.status.Status;
import com.graduates.test.resposity.PaymentResponsitory;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;  // Quan hệ nhiều-1 với UserEntity

    private double totalAmount;

    private String shippingAddress;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private OrderStatus orderStatus; // Thay đổi thành OrderStatus


    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "cart_id") // Tham chiếu đến Cart
    private Cart cart; // Liên kết tới Cart
    private String phone;
    private String receivingName;
    private LocalDateTime createdAt;
    private String note;
    private LocalDateTime deliveryDate;
    @Column(unique = true)
    private  String orderCode;

    @ManyToOne
    @JoinColumn(name = "payment_status_id")
    private PaymentStatusM paymentStatusM;

    public PaymentStatusM getPaymentStatusM() {
        return paymentStatusM;
    }

    public void setPaymentStatusM(PaymentStatusM paymentStatusM) {
        this.paymentStatusM = paymentStatusM;
    }

    public Order(UserEntity user, Payment payment, Shipment shipment, double totalAmount, String shippingAddress, LocalDateTime createdAt) {
        this.user = user;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.createdAt = createdAt;
    }
    public Order() {
    }

    public Order(Integer id, UserEntity user, double totalAmount, String shippingAddress, OrderStatus orderStatus, Payment payment, Shipment shipment, List<OrderDetail> orderDetails, Cart cart, String phone, String receivingName, LocalDateTime createdAt, String note, LocalDateTime deliveryDate, String orderCode, PaymentStatusM paymentStatusM) {
        this.id = id;
        this.user = user;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.orderStatus = orderStatus;
        this.payment = payment;
        this.shipment = shipment;
        this.orderDetails = orderDetails;
        this.cart = cart;
        this.phone = phone;
        this.receivingName = receivingName;
        this.createdAt = createdAt;
        this.note = note;
        this.deliveryDate = deliveryDate;
        this.orderCode = orderCode;
        this.paymentStatusM = paymentStatusM;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setOrder(this); // Thiết lập liên kết hai chiều
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    public void initializeFromCart(Cart cart, String shippingAddress) {
        this.user = cart.getUser(); // Lấy người dùng từ giỏ hàng
        this.shippingAddress = shippingAddress;
        this.createdAt = LocalDateTime.now();
        double total = 0.0;

        for (CartDetail cartDetail : cart.getCartDetails()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setBook(cartDetail.getBook());
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setPrice(cartDetail.getBook().getPrice());
            addOrderDetail(orderDetail); // Thêm chi tiết đơn hàng
            total += orderDetail.getPrice() * orderDetail.getQuantity();
        }
        this.totalAmount = total; // Cập nhật tổng giá trị đơn hàng
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReceivingName() {
        return receivingName;
    }

    public void setReceivingName(String receivingName) {
        this.receivingName = receivingName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
