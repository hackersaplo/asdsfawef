package com.example.employeesmanager.controller;

import com.example.employeesmanager.entity.Facility;
import com.example.employeesmanager.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.employeesmanager.dto.FacilityHierarchyDTO;
import com.example.employeesmanager.entity.DepartmentFacility;
import com.example.employeesmanager.entity.Facility;
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

    @GetMapping
    public List<Facility> getAllFacilities() {
        return facilityService.getAllFacilities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facility> getFacilityById(@PathVariable UUID id) {
        Optional<Facility> facility = facilityService.getFacilityById(id);
        return facility.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Facility createFacility(@RequestBody Facility facility) {
        return facilityService.saveFacility(facility);
    }

    @DeleteMapping("/{id}")
    public void deleteFacility(@PathVariable UUID id) {
        facilityService.deleteFacility(id);
    }

    // API lấy dữ liệu phân cấp Cơ sở -> Bộ môn -> Chuyên ngành
    @GetMapping("/hierarchy")
    public ResponseEntity<List<FacilityHierarchyDTO.FacilityDTO>> getFacilityHierarchy() {
        List<Facility> facilities = facilityService.getAllFacilities();

        List<FacilityHierarchyDTO.FacilityDTO> facilityDTOs = facilities.stream().map(facility -> {
            List<DepartmentFacility> departmentFacilities = departmentFacilityService.getByFacilityId(facility.getId());

            List<FacilityHierarchyDTO.DepartmentDTO> departmentDTOs = departmentFacilities.stream().map(df -> {
                List<MajorFacility> majorFacilities = majorFacilityService.getByDepartmentFacilityId(df.getId());

                List<FacilityHierarchyDTO.MajorDTO> majorDTOs = majorFacilities.stream().map(mf -> {
                    return new FacilityHierarchyDTO.MajorDTO(mf.getMajor().getId(), mf.getMajor().getCode(), mf.getMajor().getName());
                }).collect(Collectors.toList());

                return new FacilityHierarchyDTO.DepartmentDTO(df.getDepartment().getId(), df.getDepartment().getCode(), df.getDepartment().getName(), majorDTOs);
            }).collect(Collectors.toList());

            return new FacilityHierarchyDTO.FacilityDTO(facility.getId(), facility.getCode(), facility.getName(), departmentDTOs);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(facilityDTOs);
    }
}
