package com.example.employeesmanager.controller;

import com.example.employeesmanager.entity.Staff;
import com.example.employeesmanager.entity.StaffMajorFacility;
import com.example.employeesmanager.service.StaffMajorFacilityService;
import com.example.employeesmanager.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "http://localhost:3000")
public class StaffController {


    @Autowired
    private StaffService staffService;

    @Autowired
    private StaffMajorFacilityService staffMajorFacilityService;

    @PostMapping("/import")
    public ResponseEntity<?> importStaff(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
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
    public ResponseEntity<Void> deleteStaff(@PathVariable UUID id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<?> toggleStaffStatus(@PathVariable UUID id) {
        Staff updated = staffService.toggleStatus(id);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Lấy danh sách bộ môn chuyên ngành của nhân viên
    @GetMapping("/{id}/majors")
    public ResponseEntity<?> getStaffMajors(@PathVariable UUID id) {
        try {
            // Lấy danh sách StaffMajorFacility
            var list = staffMajorFacilityService.getByStaffId(id);
            // Map sang DTO
            var dtoList = list.stream().map(smf -> {
                var majorFacility = smf.getMajorFacility();
                var departmentFacility = majorFacility.getDepartmentFacility();
                var department = departmentFacility.getDepartment();
                var major = majorFacility.getMajor();
                var facility = departmentFacility.getFacility();
                // Giả sử status là chức vụ, có thể map số sang tên nếu cần
                String position = smf.getStatus() != null ? smf.getStatus().toString() : "";
                return new com.example.employeesmanager.dto.StaffMajorFacilityDTO(
                    smf.getId(),
                    position,
                    department != null ? department.getName() : "",
                    major != null ? major.getName() : "",
                    facility != null ? facility.getName() : ""
                );
            }).toList();
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Thêm bộ môn chuyên ngành cho nhân viên
    @PostMapping("/{id}/majors")
    public ResponseEntity<?> addStaffMajor(@PathVariable UUID id, @RequestBody StaffMajorFacility staffMajorFacility) {
        try {
            // Gán staff id cho staffMajorFacility
            staffMajorFacility.setStaff(staffService.getStaffById(id).orElseThrow(() -> new IllegalArgumentException("Nhân viên không tồn tại")));

            // Kiểm tra ràng buộc 1 bộ môn chuyên ngành trên 1 cơ sở
            UUID majorFacilityId = staffMajorFacility.getMajorFacility().getId();
            UUID facilityId = staffMajorFacilityService.getFacilityIdByMajorFacilityId(majorFacilityId);

            List<StaffMajorFacility> existingMajors = staffMajorFacilityService.getByStaffId(id);
            boolean exists = existingMajors.stream()
                .anyMatch(smf -> {
                    UUID existingFacilityId = staffMajorFacilityService.getFacilityIdByMajorFacilityId(smf.getMajorFacility().getId());
                    return existingFacilityId.equals(facilityId);
                });
            if (exists) {
                throw new IllegalArgumentException("Nhân viên đã có bộ môn chuyên ngành trong cơ sở này");
            }

            StaffMajorFacility saved = staffMajorFacilityService.save(staffMajorFacility);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint tải file template import
    @GetMapping("/import/template")
    public ResponseEntity<?> downloadImportTemplate() {
        try {
            // Đường dẫn file template trong resources/static
            String filePath = "static/import_template.xlsx";
            ClassLoader classLoader = getClass().getClassLoader();
            java.net.URL resource = classLoader.getResource(filePath);
            if (resource == null) {
                return ResponseEntity.notFound().build();
            }
            java.nio.file.Path path = java.nio.file.Paths.get(resource.toURI());
            byte[] data = java.nio.file.Files.readAllBytes(path);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=import_template.xlsx")
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi tải file template");
        }
    }



    // Xóa bộ môn chuyên ngành của nhân viên
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
