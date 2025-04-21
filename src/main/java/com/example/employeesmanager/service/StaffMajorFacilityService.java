package com.example.employeesmanager.service;

import com.example.employeesmanager.entity.StaffMajorFacility;
import com.example.employeesmanager.resposity.StaffMajorFacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StaffMajorFacilityService {

    @Autowired
    private StaffMajorFacilityRepository staffMajorFacilityRepository;

    @Autowired
    private com.example.employeesmanager.resposity.MajorFacilityRepository majorFacilityRepository;

    public List<StaffMajorFacility> getByStaffId(UUID staffId) {
        return staffMajorFacilityRepository.findByStaffId(staffId);
    }

    public StaffMajorFacility save(StaffMajorFacility entity) {
        return staffMajorFacilityRepository.save(entity);
    }

    public void deleteById(UUID id) {
        staffMajorFacilityRepository.deleteById(id);
    }

    public UUID getFacilityIdByMajorFacilityId(UUID majorFacilityId) {
        com.example.employeesmanager.entity.MajorFacility majorFacility = majorFacilityRepository.findById(majorFacilityId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bộ môn chuyên ngành"));
        return majorFacility.getDepartmentFacility().getFacility().getId();
    }
}

