package com.wastepickup.service;

import com.wastepickup.dto.ZoneAverageResponse;
import com.wastepickup.dto.ZoneRequest;
import com.wastepickup.entity.Zone;
import com.wastepickup.exception.BusinessRuleException;
import com.wastepickup.exception.DuplicateResourceException;
import com.wastepickup.exception.ResourceNotFoundException;
import com.wastepickup.repository.HouseholdRepository;
import com.wastepickup.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepo;
    private final HouseholdRepository householdRepo;

    @Transactional
    public Zone create(ZoneRequest r) {
        if (zoneRepo.existsByName(r.name())) {
            throw new DuplicateResourceException("Zone already exists: " + r.name());
        }
        Zone z = new Zone();
        z.setName(r.name());
        z.setDescription(r.description());
        return zoneRepo.save(z);
    }

    @Transactional(readOnly = true)
    public List<Zone> getAll() {
        return zoneRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Zone get(Long id) {
        return zoneRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found: " + id));
    }

    @Transactional
    public Zone update(Long id, ZoneRequest r) {
        Zone z = get(id);
        if (!z.getName().equals(r.name()) && zoneRepo.existsByName(r.name())) {
            throw new DuplicateResourceException("Zone already exists: " + r.name());
        }
        z.setName(r.name());
        z.setDescription(r.description());
        return zoneRepo.save(z);
    }

    @Transactional
    public void delete(Long id) {
        Zone z = get(id);
        if (householdRepo.existsByZoneId(id)) {
            throw new BusinessRuleException("Cannot delete a zone that still has households");
        }
        zoneRepo.delete(z);
    }

    @Transactional(readOnly = true)
    public ZoneAverageResponse averageOf(Long id) {
        Zone z = get(id);
        return new ZoneAverageResponse(z.getId(), z.getName(), round(zoneRepo.findAverageScore(id)));
    }

    @Transactional(readOnly = true)
    public List<ZoneAverageResponse> allAverages() {
        return zoneRepo.findAll().stream()
                .map(z -> new ZoneAverageResponse(z.getId(), z.getName(), round(zoneRepo.findAverageScore(z.getId()))))
                .toList();
    }

    private Double round(Double v) {
        return v == null ? null : Math.round(v * 100.0) / 100.0;
    }
}
