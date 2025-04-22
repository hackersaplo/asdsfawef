package com.example.employeesmanager.controller;

import com.example.employeesmanager.dto.StaffMajorFacilityDTO;
import com.example.employeesmanager.entity.MajorFacility;
import com.example.employeesmanager.entity.Staff;
import com.example.employeesmanager.entity.StaffMajorFacility;
import com.example.employeesmanager.service.MajorFacilityService;
import com.example.employeesmanager.service.StaffMajorFacilityService;
import com.example.employeesmanager.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "http://localhost:3000")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private StaffMajorFacilityService staffMajorFacilityService;

    @Autowired
    private MajorFacilityService majorFacilityService;

    @PostMapping("/import")
    public ResponseEntity<?> importStaff(@RequestParam("file") MultipartFile file) {
        try {
            var result = staffService.importStaffFromExcel(file);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi import file");
        }
    }

    @GetMapping
    public List<Staff> getAllStaff() {
        return staffService.getAllStaff();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable UUID id) {
        Optional<Staff> staff = staffService.getStaffById(id);
        return staff.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addStaff(@RequestBody Staff staff) {
        try {
            return ResponseEntity.ok(staffService.addStaff(staff));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable UUID id, @RequestBody Staff staff) {
        try {
            Staff updated = staffService.updateStaff(id, staff);
            return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable UUID id) {
        try {
            // Kiểm tra xem nhân viên có tồn tại không
            Optional<Staff> staffOpt = staffService.getStaffById(id);
            if (staffOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Xóa tất cả các liên kết của nhân viên trước
            List<StaffMajorFacility> staffMajors = staffMajorFacilityService.getByStaffId(id);
            for (StaffMajorFacility smf : staffMajors) {
                staffMajorFacilityService.deleteById(smf.getId());
            }

            // Sau đó xóa nhân viên
            staffService.deleteStaff(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi khi xóa nhân viên: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<?> toggleStaffStatus(@PathVariable UUID id) {
        Staff updated = staffService.toggleStatus(id);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/majors")
    public ResponseEntity<?> getStaffMajors(@PathVariable UUID id) {
        try {
            var list = staffMajorFacilityService.getByStaffId(id);
            var dtoList = list.stream().map(smf -> {
                var majorFacility = smf.getMajorFacility();
                var departmentFacility = majorFacility.getDepartmentFacility();
                var department = departmentFacility.getDepartment();
                var major = majorFacility.getMajor();
                var facility = departmentFacility.getFacility();

                String position = "";
                if (smf.getStatus() != null) {
                    switch (smf.getStatus()) {
                        case 1: position = "Giảng viên"; break;
                        case 2: position = "Trưởng bộ môn"; break;
                        case 3: position = "Phó trưởng bộ môn"; break;
                        default: position = "Khác";
                    }
                }

                return new StaffMajorFacilityDTO(
                        smf.getId(),
                        position,
                        department != null ? department.getName() : "",
                        major != null ? major.getName() : "",
                        facility != null ? facility.getName() : ""
                );
            }).collect(Collectors.toList());

            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/majors")
    public ResponseEntity<?> addStaffMajor(@PathVariable UUID id, @RequestBody StaffMajorFacility staffMajorFacility) {
        try {
            Staff staff = staffService.getStaffById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Nhân viên không tồn tại"));

            staffMajorFacility.setStaff(staff);

            // Đảm bảo majorFacility đã được load đầy đủ
            MajorFacility majorFacility = majorFacilityService.getById(staffMajorFacility.getMajorFacility().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Bộ môn chuyên ngành không tồn tại"));

            // Lấy thông tin facility từ majorFacility
            UUID newFacilityId = majorFacility.getDepartmentFacility().getFacility().getId();

            // Kiểm tra ràng buộc 1 bộ môn chuyên ngành trên 1 cơ sở
            List<StaffMajorFacility> existingMajors = staffMajorFacilityService.getByStaffId(id);
            boolean exists = existingMajors.stream()
                    .anyMatch(smf -> smf.getMajorFacility()
                            .getDepartmentFacility()
                            .getFacility()
                            .getId()
                            .equals(newFacilityId));

            if (exists) {
                throw new IllegalArgumentException("Nhân viên đã có bộ môn chuyên ngành trong cơ sở này");
            }

            staffMajorFacility.setId(UUID.randomUUID());
            staffMajorFacility.setCreatedDate(System.currentTimeMillis());
            staffMajorFacility.setLastModifiedDate(System.currentTimeMillis());

            StaffMajorFacility saved = staffMajorFacilityService.save(staffMajorFacility);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/import/template")
    public ResponseEntity<Resource> downloadImportTemplate() throws IOException {
        Resource resource = new ClassPathResource("templates/import_template.xlsx");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=import_template.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }

    @DeleteMapping("/{id}/majors/{majorId}")
    public ResponseEntity<?> deleteStaffMajor(@PathVariable UUID id, @PathVariable UUID majorId) {
        try {
            staffMajorFacilityService.deleteById(majorId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}