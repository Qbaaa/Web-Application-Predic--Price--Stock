package com.qbaaa.stockpricepredict.repository;

import java.util.Optional;

import com.qbaaa.stockpricepredict.models.ERole;
import com.qbaaa.stockpricepredict.models.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRole(ERole role);
}
