package com.qbaaa.stockpricepredict.repository;

import com.qbaaa.stockpricepredict.models.StatusUpdateCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface StatusUpdateCompanyRepository extends JpaRepository<StatusUpdateCompany, Long> {

    Boolean existsById(long id);
    Boolean existsByDate(LocalDate date);
}
