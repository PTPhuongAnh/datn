package com.graduates.test.model;
import java.time.LocalDateTime;
import java.util.List;
public class OrderRequest {
    private String shippingAddress; // Địa chỉ nhận hàng
    private List<Integer> selectedCartDetailIds; // Danh sách ID của các chi tiết giỏ hàng được chọn
    private Integer paymentId; // ID của payment
    private Integer shipmentId; // ID của shipment

    // Constructors
    public OrderRequest() {}

    public OrderRequest(String shippingAddress, List<Integer> selectedCartDetailIds, Integer paymentId, Integer shipmentId) {
        this.shippingAddress = shippingAddress;
        this.selectedCartDetailIds = selectedCartDetailIds;
        this.paymentId = paymentId;
        this.shipmentId = shipmentId;
    }

    // Getters and Setters
    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<Integer> getSelectedCartDetailIds() {
        return selectedCartDetailIds;
    }

    public void setSelectedCartDetailIds(List<Integer> selectedCartDetailIds) {
        this.selectedCartDetailIds = selectedCartDetailIds;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
    }


}
