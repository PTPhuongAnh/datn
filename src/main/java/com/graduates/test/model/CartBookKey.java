package com.graduates.test.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
@Data
@Embeddable
public class CartBookKey implements Serializable {
    @Column(name = "cart_id")
    private Integer cartId;

    @Column(name = "book_id")
    private Integer bookId;

    public CartBookKey() {
    }

    public CartBookKey(Integer cartId, Integer bookId) {
        this.cartId = cartId;
        this.bookId = bookId;
    }
}
