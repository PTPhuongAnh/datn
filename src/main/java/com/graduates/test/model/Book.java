package com.graduates.test.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_book")
    private int idBook;
    private String nameBook;
   private String author;

//    private String image;
    private String description;
    private int quantity;
    private int price;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    @ManyToOne // Xác định rằng có nhiều sách có thể thuộc một thể loại (Category)
    @JoinColumn(name = "id_category") // Tên cột trong bảng 'book' dùng để ánh xạ với Category
    private Category category;
    @ManyToOne // Xác định rằng nhiều sách có thể thuộc về một nhà xuất bản
    @JoinColumn(name = "id_publisher") // Cột liên kết với Publisher
    private Publisher publisher;
//    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL) 
//    @OneToMany(mappedBy = "book")
 //   @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ImageBook> images; // Danh sách hình ảnh liên quan đến sách
@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageBook> imageBooks = new ArrayList<>();
    public Book() {
    }

    public Book(int idBook) {
        this.idBook = idBook;
    }

//    public Book(int idBook, String nameBook, String author, String description, Category category, Publisher publisher) {
//        this.idBook = idBook;
//        this.nameBook = nameBook;
//        this.author = author;
//
//        this.description = description;
//        this.category = category;
//        this.publisher = publisher;
//    }


    public Book(int idBook, String nameBook, String author, String description,  Category category, Publisher publisher, int quantity, int price,List<ImageBook> imageBooks) {
        this.idBook = idBook;
        this.nameBook = nameBook;
        this.author = author;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.publisher = publisher;
        this.imageBooks = imageBooks;
    }

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
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



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }



    public LocalDateTime getUpdateAt() {
        return updateAt;
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

    public List<ImageBook> getImageBooks() {
        return imageBooks;
    }

    public void setImageBooks(List<ImageBook> imageBooks) {
        this.imageBooks = imageBooks;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
