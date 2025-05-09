package com.example.employeesmanager.service;

import com.example.employeesmanager.entity.DepartmentFacility;
import com.example.employeesmanager.resposity.DepartmentFacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DepartmentFacilityService {

    @Autowired
    private DepartmentFacilityRepository departmentFacilityRepository;

    public List<DepartmentFacility> getByDepartmentId(UUID departmentId) {
        return departmentFacilityRepository.findByDepartmentId(departmentId);
    }

    public DepartmentFacility save(DepartmentFacility entity) {
        return departmentFacilityRepository.save(entity);
    }

    public void deleteById(UUID id) {
        departmentFacilityRepository.deleteById(id);
    }

    public List<DepartmentFacility> getByFacilityId(UUID id) {
        return departmentFacilityRepository.findByFacilityId(id);
    }

    public Optional<DepartmentFacility> findByFacilityIdAndDepartmentId(UUID facilityId, UUID departmentId) {
        return departmentFacilityRepository.findByFacilityIdAndDepartmentId(facilityId, departmentId);
    }
}