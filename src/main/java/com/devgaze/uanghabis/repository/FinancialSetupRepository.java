package com.devgaze.uanghabis.repository;

import com.devgaze.uanghabis.entity.FinancialSetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FinancialSetupRepository extends JpaRepository<FinancialSetup, UUID> {

}
