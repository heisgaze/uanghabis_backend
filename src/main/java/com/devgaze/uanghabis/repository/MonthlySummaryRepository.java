package com.devgaze.uanghabis.repository;

import com.devgaze.uanghabis.entity.MonthlySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MonthlySummaryRepository extends JpaRepository<MonthlySummary, UUID> {
}
