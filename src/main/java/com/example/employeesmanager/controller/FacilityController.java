package com.example.employeesmanager.controller;

import com.example.employeesmanager.entity.Facility;
import com.example.employeesmanager.entity.Department;
import com.example.employeesmanager.entity.DepartmentFacility;
import com.example.employeesmanager.entity.Major;
import com.example.employeesmanager.entity.MajorFacility;
import com.example.employeesmanager.service.DepartmentFacilityService;
import com.example.employeesmanager.service.FacilityService;
import com.example.employeesmanager.service.MajorFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private DepartmentFacilityService departmentFacilityService;

    @Autowired
    private MajorFacilityService majorFacilityService;

    @GetMapping("/{facilityId}/departments")
    public ResponseEntity<?> getDepartmentsByFacility(@PathVariable UUID facilityId) {
        try {
            Optional<Facility> facilityOpt = facilityService.getFacilityById(facilityId);
            if (facilityOpt.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            List<DepartmentFacility> departmentFacilities = departmentFacilityService.getByFacilityId(facilityId);

            List<Department> departments = departmentFacilities.stream()
                    .map(DepartmentFacility::getDepartment)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{facilityId}/departments/{departmentId}/majors")
    public ResponseEntity<?> getMajorsByDepartmentAndFacility(
            @PathVariable UUID facilityId,
            @PathVariable UUID departmentId) {
        try {
            DepartmentFacility departmentFacility = departmentFacilityService
                    .findByFacilityIdAndDepartmentId(facilityId, departmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bộ môn tại cơ sở này"));

            List<MajorFacility> majorFacilities = majorFacilityService.getByDepartmentFacilityId(departmentFacility.getId());
            List<Major> majors = majorFacilities.stream()
                    .map(MajorFacility::getMajor)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(majors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllFacilities() {
        try {
            List<Facility> facilities = facilityService.getAllFacilities();
            return ResponseEntity.ok(facilities);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addFacility(@RequestBody Facility facility) {
        try {
            Facility savedFacility = facilityService.saveFacility(facility);
            return ResponseEntity.ok(savedFacility);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
