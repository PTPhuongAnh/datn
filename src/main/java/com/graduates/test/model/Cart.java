package com.graduates.test.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cart")
    private Integer idCart;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id_user")
    private UserEntity user; // Mối quan hệ với User

    private double totalPrice; // Tổng giá trị giỏ hàng

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartDetail> cartDetails = new ArrayList<>();

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

    public Cart() {}
    public void addCartDetail(CartDetail cartDetail) {
        cartDetails.add(cartDetail);
        cartDetail.setCart(this); // Đảm bảo liên kết ngược giữa Cart và CartDetail
        updateTotalPrice(); // Cập nhật tổng giá trị giỏ hàng sau khi thêm chi tiết
    }

    public Integer getIdCart() {
        return idCart;
    }

    public void setIdCart(Integer idCart) {
        this.idCart = idCart;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCartDetails(List<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }

    public List<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public void addBook(Book book, int quantity) {
        CartDetail cartDetail = new CartDetail(this, book, quantity);
        cartDetails.add(cartDetail);
        updateTotalPrice();
    }

    public void updateTotalPrice() {
        totalPrice = cartDetails.stream()
                .mapToDouble(cd -> cd.getQuantity() * cd.getBook().getPrice())
                .sum();
    }

    public void removeBook(Book book) {
        cartDetails.removeIf(cd -> cd.getBook().equals(book));
        updateTotalPrice();
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
