package com.example.employeesmanager.resposity;

import com.example.employeesmanager.entity.StaffMajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StaffMajorFacilityRepository extends JpaRepository<StaffMajorFacility, UUID> {
    void deleteByStaffId(UUID staffId);
    List<StaffMajorFacility> findByStaffId(UUID staffId);
}

