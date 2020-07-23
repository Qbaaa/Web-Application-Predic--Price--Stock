package com.qbaaa.stockpricepredict.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "predict_price_companies")
public class PredictPriceCompanies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private String windowSize;

    private LocalDate date;

    private Double priceClose;

    public PredictPriceCompanies() {
    }

    public PredictPriceCompanies(String symbol, String windowSize, LocalDate date, Double priceClose) {
        this.symbol = symbol;
        this.windowSize = windowSize;
        this.date = date;
        this.priceClose = priceClose;
    }

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getPriceClose() {
        return priceClose;
    }

    public String getWindowSize() {
        return windowSize;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPriceClose(Double priceClose) {
        this.priceClose = priceClose;
    }

    public void setWindowSize(String windowSize) {
        this.windowSize = windowSize;
    }
}
