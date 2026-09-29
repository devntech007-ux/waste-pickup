package com.wastepickup.repository;

import com.wastepickup.entity.Household;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseholdRepository extends JpaRepository<Household, Long> {

    List<Household> findByFlaggedTrue();

    boolean existsByZoneId(Long zoneId);
}
