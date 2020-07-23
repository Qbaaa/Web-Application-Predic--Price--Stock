package com.qbaaa.stockpricepredict.repository;

import com.qbaaa.stockpricepredict.models.PredictPriceCompanies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictPriceCompaniesRepository extends JpaRepository<PredictPriceCompanies,Long> {

    List<PredictPriceCompanies> findBySymbolAndWindowSize(String symbol, String windowSize);
}
