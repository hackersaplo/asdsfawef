package com.example.employeesmanager.resposity;

import com.example.employeesmanager.entity.DepartmentFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DepartmentFacilityRepository extends JpaRepository<DepartmentFacility, UUID> {
    List<DepartmentFacility> findByDepartmentId(UUID departmentId);
}