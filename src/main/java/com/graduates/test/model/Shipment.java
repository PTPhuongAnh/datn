package com.graduates.test.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "shipment")
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idShipment;

    private String shippingMethod; // Phương thức vận chuyển (VD: Giao hàng nhanh, Giao hàng tiêu chuẩn)

    @OneToOne(mappedBy = "shipment")
    private Order order; // Liên kết với Order

    public Shipment() {}

    public Shipment(Order order, String shippingMethod) {
        this.order = order;
        this.shippingMethod = shippingMethod;
    }

    public Integer getIdShipment() {
        return idShipment;
    }

    public void setIdShipment(Integer idShipment) {
        this.idShipment = idShipment;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
