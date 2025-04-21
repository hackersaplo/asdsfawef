package com.example.employeesmanager.controller;

import com.example.employeesmanager.dto.FacilityHierarchyDTO;
import com.example.employeesmanager.entity.*;
import com.example.employeesmanager.service.*;
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

    // ... existing endpoints ...

    @GetMapping("/{facilityId}/departments")
    public ResponseEntity<?> getDepartmentsByFacility(@PathVariable UUID facilityId) {
        try {
            System.out.println("Đang lấy bộ môn cho cơ sở ID: " + facilityId);

            Optional<Facility> facilityOpt = facilityService.getFacilityById(facilityId);
            if (facilityOpt.isEmpty()) {
                System.out.println("Không tìm thấy cơ sở với ID: " + facilityId);
                return ResponseEntity.ok(Collections.emptyList());
            }

            List<DepartmentFacility> departmentFacilities = departmentFacilityService.getByFacilityId(facilityId);
            System.out.println("Số bộ môn tìm thấy: " + departmentFacilities.size());

            List<Department> departments = departmentFacilities.stream()
                    .map(DepartmentFacility::getDepartment)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

            System.out.println("Danh sách bộ môn distinct: " + departments.size());
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy bộ môn: " + e.getMessage());
            e.printStackTrace();
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
}