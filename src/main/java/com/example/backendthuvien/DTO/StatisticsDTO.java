package com.example.backendthuvien.DTO;

public class StatisticsDTO {
    private String productName;
    private int period; // Week hoặc Month
    private int year;
    private Long totalQuantity;

    // Constructor
    public StatisticsDTO(String productName, int period, int year, Long totalQuantity) {
        this.productName = productName;
        this.period = period;
        this.year = year;
        this.totalQuantity = totalQuantity;
    }

    // Getter và Setter
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
