package com.qbaaa.stockpricepredict.response;

import java.time.LocalDate;
import java.util.List;

public class CompanyResponse {

    private List<LocalDate> datesTrain;
    private List<Double> pricesActualTrain;
    private List<Double> pricePredicTrain;
    private LocalDate dateNextDay;
    private Double priceNextDay;

    public CompanyResponse(List<LocalDate> datesTrain, List<Double> pricesActualTrain, List<Double> pricePredicTrain, LocalDate dateNextDay, Double priceNextDay) {
        this.datesTrain = datesTrain;
        this.pricesActualTrain = pricesActualTrain;
        this.pricePredicTrain = pricePredicTrain;
        this.dateNextDay = dateNextDay;
        this.priceNextDay = priceNextDay;
    }

    public List<LocalDate> getDatesTrain() {
        return datesTrain;
    }

    public List<Double> getPricesActualTrain() {
        return pricesActualTrain;
    }

    public List<Double> getPricePredicTrain() {
        return pricePredicTrain;
    }

    public LocalDate getDateNextDay() {
        return dateNextDay;
    }

    public Double getPriceNextDay() {
        return priceNextDay;
    }

    public void setDatesTrain(List<LocalDate> datesTrain) {
        this.datesTrain = datesTrain;
    }

    public void setPricesActualTrain(List<Double> pricesActualTrain) {
        this.pricesActualTrain = pricesActualTrain;
    }

    public void setPricePredicTrain(List<Double> pricePredicTrain) {
        this.pricePredicTrain = pricePredicTrain;
    }

    public void setDateNextDay(LocalDate dateNextDay) {
        this.dateNextDay = dateNextDay;
    }

    public void setPriceNextDay(Double priceNextDay) {
        this.priceNextDay = priceNextDay;
    }
}
