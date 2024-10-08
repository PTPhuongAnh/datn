package com.graduates.test.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private  Integer idUser;
    private String username;
    private String password;
    private String email;
    private String fullname;
    private String dob;
    private String phone;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",joinColumns =@JoinColumn(name = "user_id",referencedColumnName = "id_user" ),
    inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "idRoles"))
    private List<Role> roles= new ArrayList<>();
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address; // Mối quan hệ một-một với Address
//    @OneToOne(mappedBy = "cart",cascade = CascadeType.ALL)
//    private Cart cart; // Mối quan hệ một-một với Cart
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart cart; // Mối quan hệ với Cart


//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Order> orders; // Liên kết với các Order


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();  // Quan hệ 1-nhiều với Order

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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public UserEntity(Integer idUser, String username, String password, List<Role> roles) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UserEntity(Integer idUser) {
        this.idUser = idUser;
    }

    public UserEntity(String username, String password, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
