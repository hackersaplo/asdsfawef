package com.example.employeesmanager.resposity;

import com.example.employeesmanager.entity.DepartmentFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentFacilityRepository extends JpaRepository<DepartmentFacility, UUID> {
    List<DepartmentFacility> findByDepartmentId(UUID departmentId);
    List<DepartmentFacility> findByFacilityId(UUID facilityId);
    Optional<DepartmentFacility> findByFacilityIdAndDepartmentId(UUID facilityId, UUID departmentId);
}