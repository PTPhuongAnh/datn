package com.graduates.test.model;

public class StatusRespone {
    private boolean status;
    private String message;
    private Integer bookId;

    public StatusRespone(boolean status, String message, Integer bookId) {
        this.status = status;
        this.message = message;
        this.bookId = bookId;
    }

    public StatusRespone(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}
