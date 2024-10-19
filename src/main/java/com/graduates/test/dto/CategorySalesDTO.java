package com.graduates.test.dto;

public class CategorySalesDTO {

    private String categoryName;
    private Long booksSold;

    public CategorySalesDTO(String categoryName, Long booksSold) {
        this.categoryName = categoryName;
        this.booksSold = booksSold;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getBooksSold() {
        return booksSold;
    }

    public void setBooksSold(Long booksSold) {
        this.booksSold = booksSold;
    }
}
