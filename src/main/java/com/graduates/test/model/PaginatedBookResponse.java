package com.graduates.test.model;

import com.graduates.test.dto.BookRespone;

import java.util.List;

public class PaginatedBookResponse {
    private List<BookRespone> books;  // Danh sách sách
    private int currentPage;          // Trang hiện tại
    private long totalItems;          // Tổng số bản ghi
    private int totalPages;           // Tổng số trang

    public PaginatedBookResponse() {}

    public PaginatedBookResponse(List<BookRespone> books, int currentPage, long totalItems, int totalPages) {
        this.books = books;
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    // Getters và setters
    public List<BookRespone> getBooks() {
        return books;
    }

    public void setBooks(List<BookRespone> books) {
        this.books = books;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
