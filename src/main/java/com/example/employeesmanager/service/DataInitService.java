package com.example.employeesmanager.service;

import com.example.employeesmanager.entity.*;
import com.example.employeesmanager.resposity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DataInitService {

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentFacilityRepository departmentFacilityRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private MajorFacilityRepository majorFacilityRepository;

    @Transactional
    public void initSampleData() {
        if (facilityRepository.count() > 0) {
            // Data already exists, skip initialization
            return;
        }

        // Create sample Facility
        Facility facility = new Facility();
        facility.setId(UUID.randomUUID());
        facility.setName("Sample Facility");
        facility.setStatus(1);
        facilityRepository.save(facility);

        // Create sample Department
        Department department = new Department();
        department.setId(UUID.randomUUID());
        department.setName("Sample Department");
        department.setStatus(1);
        departmentRepository.save(department);

        // Create DepartmentFacility
        DepartmentFacility departmentFacility = new DepartmentFacility();
        departmentFacility.setId(UUID.randomUUID());
        departmentFacility.setFacility(facility);
        departmentFacility.setDepartment(department);
        departmentFacility.setStatus(1);
        departmentFacilityRepository.save(departmentFacility);

        // Create sample Major
        Major major = new Major();
        major.setId(UUID.randomUUID());
        major.setName("Sample Major");
        major.setStatus(1);
        majorRepository.save(major);

        // Create MajorFacility
        MajorFacility majorFacility = new MajorFacility();
        majorFacility.setId(UUID.randomUUID());
        majorFacility.setDepartmentFacility(departmentFacility);
        majorFacility.setMajor(major);
        majorFacility.setStatus(1);
        majorFacilityRepository.save(majorFacility);
    }
}
