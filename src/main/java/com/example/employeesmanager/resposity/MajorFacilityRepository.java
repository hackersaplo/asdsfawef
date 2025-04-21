package com.example.employeesmanager.resposity;

import com.example.employeesmanager.entity.Major;
import com.example.employeesmanager.entity.MajorFacility;
import com.example.employeesmanager.entity.DepartmentFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MajorFacilityRepository extends JpaRepository<MajorFacility, UUID> {
    List<MajorFacility> findByMajorId(UUID majorId);
    List<MajorFacility> findByDepartmentFacilityId(UUID departmentFacilityId);

    @Query("SELECT mf FROM MajorFacility mf WHERE mf.major.id = :majorId AND mf.departmentFacility.id = :departmentFacilityId")
    Optional<MajorFacility> findByMajorIdAndDepartmentFacilityId(@Param("majorId") UUID majorId, @Param("departmentFacilityId") UUID departmentFacilityId);
}
