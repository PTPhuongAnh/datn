package com.graduates.test.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

@Data
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_book")
    private Integer idBook;
    @NotNull(message = "this is required")
    @NotBlank(message = "this is required")
    private String nameBook;
    @NotNull(message = "this is required")
    @NotBlank(message = "this is required")
    private String author;
    @NotNull(message = "this is required")
    @NotBlank(message = "this is required")
    private String description_short;
    @NotNull(message = "this is required")
    @NotBlank(message = "this is required")
    private String description_long;
    @NotNull(message = "this is required")
    @NotBlank(message = "this is required")
    private String size;
    @NotNull(message = "this is required")
    @NotBlank(message = "this is required")
    private String year_publisher;
    @NotNull(message = "this is required")
    @NotBlank(message = "this is required")
    private String page_number;
    @NotNull(message = "this is required")
    @NotBlank(message = "this is required")
    private String barcode;
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    @Min(value = 1, message = "Price must be at least 1")
    private Integer quantity;
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    @Min(value = 1, message = "Price must be at least 1")
    private Integer price;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    @ManyToOne // Xác định rằng có nhiều sách có thể thuộc một thể loại (Category)
    @JoinColumn(name = "id_category") // Tên cột trong bảng 'book' dùng để ánh xạ với Category
    private Category category;
    @ManyToOne // Xác định rằng nhiều sách có thể thuộc về một nhà xuất bản
    @JoinColumn(name = "id_publisher") // Cột liên kết với Publisher
    private Publisher publisher;
    @ManyToOne
    @JoinColumn(name = "id_distributor")
    private Distributor distributor;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageBook> imageBooks = new ArrayList<>();
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartDetail> cartDetails = new HashSet<>();

    private boolean isDeleted=false;
    public Book(Integer bookId) {
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

    public Book() {
    }

    public Book(Integer idBook, String nameBook, String author, String description_short, String description_long, String size, String year_publisher, String page_number, String barcode, Integer quantity, Integer price, Category category, Publisher publisher, Distributor distributor, List<ImageBook> imageBooks) {
        this.idBook = idBook;
        this.nameBook = nameBook;
        this.author = author;
        this.description_short = description_short;
        this.description_long = description_long;
        this.size = size;
        this.year_publisher = year_publisher;
        this.page_number = page_number;
        this.barcode = barcode;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.publisher = publisher;
        this.distributor = distributor;
        this.imageBooks = imageBooks;
    }

    public Integer getIdBook() {
        return idBook;
    }

    public void setIdBook(Integer idBook) {
        this.idBook = idBook;
    }

    public String getNameBook() {
        return nameBook;
    }

    public void setNameBook(String nameBook) {
        this.nameBook = nameBook;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription_short() {
        return description_short;
    }

    public void setDescription_short(String description_short) {
        this.description_short = description_short;
    }

    public String getDescription_long() {
        return description_long;
    }

    public void setDescription_long(String description_long) {
        this.description_long = description_long;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getYear_publisher() {
        return year_publisher;
    }

    public void setYear_publisher(String year_publisher) {
        this.year_publisher = year_publisher;
    }

    public String getPage_number() {
        return page_number;
    }

    public void setPage_number(String page_number) {
        this.page_number = page_number;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public List<ImageBook> getImageBooks() {
        return imageBooks;
    }

    public void setImageBooks(List<ImageBook> imageBooks) {
        this.imageBooks = imageBooks;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Set<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(Set<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
