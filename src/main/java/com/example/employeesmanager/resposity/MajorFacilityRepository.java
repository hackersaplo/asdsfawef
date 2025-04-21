package com.example.employeesmanager.resposity;

import com.example.employeesmanager.entity.Major;
import com.example.employeesmanager.entity.MajorFacility;
import com.example.employeesmanager.entity.DepartmentFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MajorFacilityRepository extends JpaRepository<MajorFacility, UUID> {
    List<MajorFacility> findByMajorId(UUID majorId);
    List<MajorFacility> findByDepartmentFacilityId(UUID departmentFacilityId);
    Optional<MajorFacility> findByMajorAndDepartmentFacility(Major major, DepartmentFacility departmentFacility);
}