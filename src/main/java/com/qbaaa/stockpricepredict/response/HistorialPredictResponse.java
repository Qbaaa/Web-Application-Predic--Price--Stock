package com.qbaaa.stockpricepredict.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistorialPredictResponse {

    private String symbol;
    private String windowSize;
    private List<LocalDate> datesTest = new ArrayList<>();
    private List<Double> pricesActualTest = new ArrayList<>();
    private List<Double> pricePredicTest = new ArrayList<>();

    public HistorialPredictResponse() { }

    public HistorialPredictResponse(String symbol, String windowSize, List<LocalDate> datesTest, List<Double> pricesActualTest, List<Double> pricePredicTest) {
        this.symbol = symbol;
        this.windowSize = windowSize;
        this.datesTest = datesTest;
        this.pricesActualTest = pricesActualTest;
        this.pricePredicTest = pricePredicTest;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getWindowSize() {
        return windowSize;
    }

    public List<LocalDate> getDatesTest() {
        return datesTest;
    }

    public List<Double> getPricesActualTest() {
        return pricesActualTest;
    }

    public List<Double> getPricePredicTest() {
        return pricePredicTest;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setWindowSize(String windowSize) {
        this.windowSize = windowSize;
    }

    public void setDatesTest(List<LocalDate> datesTest) {
        this.datesTest = datesTest;
    }

    public void setPricesActualTest(List<Double> pricesActualTest) {
        this.pricesActualTest = pricesActualTest;
    }

    public void setPricePredicTest(List<Double> pricePredicTest) {
        this.pricePredicTest = pricePredicTest;
    }

    public void addDatesTest(LocalDate datesTest) {
        this.datesTest.add(datesTest);
    }

    public void addPricesActualTest(Double pricesActualTest) {
        this.pricesActualTest.add(pricesActualTest);
    }

    public void addPricePredicTest(Double pricePredicTest) {
        this.pricePredicTest.add(pricePredicTest);
    }
}
