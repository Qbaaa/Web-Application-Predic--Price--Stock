package com.qbaaa.stockpricepredict.repository;

import com.qbaaa.stockpricepredict.models.HistoricalPriceCompanies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface HistoricalPriceCompaniesRepository extends JpaRepository<HistoricalPriceCompanies, Long> {

    HistoricalPriceCompanies findBySymbolAndDate(String symbol, LocalDate date);
}
