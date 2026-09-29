package com.wastepickup.repository;

import com.wastepickup.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ZoneRepository extends JpaRepository<Zone, Long> {

    boolean existsByName(String name);

    @Query("select avg(h.rollingAverage) from Household h " +
           "where h.zone.id = :zoneId and h.rollingAverage is not null")
    Double findAverageScore(@Param("zoneId") Long zoneId);
}
