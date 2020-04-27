package com.qbaaa.stockpricepredict.models;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private LocalDate date;

    private Double priceClose;

    public Company() {
    }

    public Company(String symbol, LocalDate date, Double priceClose) {
        this.symbol = symbol;
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

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", date=" + date +
                ", priceClose=" + priceClose +
                '}';
    }
}
