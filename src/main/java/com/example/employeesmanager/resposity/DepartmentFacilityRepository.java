package com.example.employeesmanager.resposity;

import com.example.employeesmanager.entity.DepartmentFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentFacilityRepository extends JpaRepository<DepartmentFacility, UUID> {
    List<DepartmentFacility> findByDepartmentId(UUID departmentId);
    List<DepartmentFacility> findByFacilityId(UUID facilityId);

    @Query("SELECT df FROM DepartmentFacility df WHERE df.facility.id = :facilityId AND df.department.id = :departmentId")
    Optional<DepartmentFacility> findByFacilityIdAndDepartmentId(@Param("facilityId") UUID facilityId, @Param("departmentId") UUID departmentId);
}
