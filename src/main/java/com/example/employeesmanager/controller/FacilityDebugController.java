package com.example.employeesmanager.controller;

import com.example.employeesmanager.entity.*;
import com.example.employeesmanager.service.*;
import com.example.employeesmanager.resposity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/facilities")
public class FacilityDebugController {

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentFacilityRepository departmentFacilityRepository;

    @Autowired
    private MajorService majorService;

    @Autowired
    private MajorFacilityRepository majorFacilityRepository;

    @GetMapping("/debug")
    public ResponseEntity<?> debugData() {
        Map<String, Object> data = new HashMap<>();

        try {
            // Kiểm tra facilities
            List<Facility> facilities = facilityService.getAllFacilities();
            data.put("facilitiesCount", facilities.size());
            if (!facilities.isEmpty()) {
                data.put("facilities", facilities);
            }

            // Kiểm tra departments
            List<Department> departments = departmentService.getAllDepartments();
            data.put("departmentsCount", departments.size());
            if (!departments.isEmpty()) {
                data.put("departments", departments);
            }

            // Kiểm tra departmentFacilities
            List<DepartmentFacility> dfs = departmentFacilityRepository.findAll();
            data.put("departmentFacilitiesCount", dfs.size());
            if (!dfs.isEmpty()) {
                List<Map<String, Object>> dfMaps = dfs.stream().map(df -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", df.getId());
                    map.put("departmentId", df.getDepartment() != null ? df.getDepartment().getId() : null);
                    map.put("facilityId", df.getFacility() != null ? df.getFacility().getId() : null);
                    return map;
                }).collect(Collectors.toList());
                data.put("departmentFacilities", dfMaps);
            }

            // Kiểm tra majors
            List<Major> majors = majorService.getAllMajors();
            data.put("majorsCount", majors.size());
            if (!majors.isEmpty()) {
                data.put("majors", majors);
            }

            // Kiểm tra majorFacilities
            List<MajorFacility> mfs = majorFacilityRepository.findAll();
            data.put("majorFacilitiesCount", mfs.size());
            if (!mfs.isEmpty()) {
                data.put("majorFacilities", mfs);
            }

            return ResponseEntity.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
