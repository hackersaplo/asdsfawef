package com.example.employeesmanager.resposity;

import com.example.employeesmanager.entity.StaffMajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StaffMajorFacilityRepository extends JpaRepository<StaffMajorFacility, UUID> {
    @Modifying
    @Transactional
    @Query("delete from StaffMajorFacility smf where smf.staff.id = ?1")
    void deleteByStaffId(UUID staffId);

    List<StaffMajorFacility> findByStaffId(UUID staffId);

    java.util.Optional<StaffMajorFacility> findByStaffIdAndMajorFacilityId(UUID staffId, UUID majorFacilityId);
}

