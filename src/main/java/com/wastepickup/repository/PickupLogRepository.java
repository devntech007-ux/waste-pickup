package com.wastepickup.repository;

import com.wastepickup.entity.PickupLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PickupLogRepository extends JpaRepository<PickupLog, Long> {

    List<PickupLog> findByHouseholdIdOrderByPickupTimeDesc(Long householdId);

    List<PickupLog> findByHouseholdIdOrderByPickupTimeDesc(Long householdId, Pageable pageable);

    void deleteByHouseholdId(Long householdId);
}
