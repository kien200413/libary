package com.example.backendthuvien.entity;

import lombok.Builder;

import java.util.List;

public class RegisterRespone {
    private String message;
    private List<String> errors; // Thêm thuộc tính errors

    // Getter và Setter cho errors
    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    // Builder pattern nếu bạn đang dùng Lombok
    @Builder
    public RegisterRespone(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }
}
