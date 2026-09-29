package com.wastepickup.service;

import com.wastepickup.dto.HouseholdRequest;
import com.wastepickup.entity.Household;
import com.wastepickup.exception.ResourceNotFoundException;
import com.wastepickup.repository.HouseholdRepository;
import com.wastepickup.repository.PickupLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseholdService {

    private final HouseholdRepository householdRepo;
    private final PickupLogRepository logRepo;
    private final ZoneService zoneService;

    @Transactional
    public Household create(HouseholdRequest r) {
        Household h = new Household();
        h.setOwnerName(r.ownerName());
        h.setAddress(r.address());
        h.setZone(zoneService.get(r.zoneId()));
        return householdRepo.save(h);
    }

    @Transactional(readOnly = true)
    public List<Household> getAll() {
        return householdRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Household get(Long id) {
        return householdRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Household not found: " + id));
    }

    @Transactional
    public Household update(Long id, HouseholdRequest r) {
        Household h = get(id);
        h.setOwnerName(r.ownerName());
        h.setAddress(r.address());
        h.setZone(zoneService.get(r.zoneId()));
        return householdRepo.save(h);
    }

    /** Deletes the household together with its pickup history. */
    @Transactional
    public void delete(Long id) {
        Household h = get(id);
        logRepo.deleteByHouseholdId(id);
        householdRepo.delete(h);
    }

    @Transactional(readOnly = true)
    public List<Household> getFlagged() {
        return householdRepo.findByFlaggedTrue();
    }
}
