package com.example.employeesmanager.service;

import com.example.employeesmanager.entity.*;
import com.example.employeesmanager.resposity.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
public class StaffService {
    @Autowired
    private DepartmentFacilityRepository departmentFacilityRepository;
    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private ImportHistoryRepository importHistoryRepository;

    @Autowired
    private StaffMajorFacilityRepository staffMajorFacilityRepository;

    @Autowired
    private MajorFacilityRepository majorFacilityRepository;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private MajorRepository majorRepository;

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Optional<Staff> getStaffById(UUID id) {
        return staffRepository.findById(id);
    }

    public Staff addStaff(Staff staff) {
        validateStaff(staff, null);
        staff.setCreatedDate(System.currentTimeMillis());
        staff.setLastModifiedDate(System.currentTimeMillis());
        return staffRepository.save(staff);
    }

    public Staff updateStaff(UUID id, Staff updatedStaff) {
        return staffRepository.findById(id).map(existing -> {
            validateStaff(updatedStaff, id);
            existing.setAccountFe(updatedStaff.getAccountFe());
            existing.setAccountFpt(updatedStaff.getAccountFpt());
            existing.setName(updatedStaff.getName());
            existing.setStaffCode(updatedStaff.getStaffCode());
            existing.setStatus(updatedStaff.getStatus());
            existing.setLastModifiedDate(System.currentTimeMillis());
            return staffRepository.save(existing);
        }).orElse(null);
    }

    public void deleteStaff(UUID id) {
        staffRepository.deleteById(id);
    }

    public Staff toggleStatus(UUID id) {
        return staffRepository.findById(id).map(staff -> {
            staff.setStatus(staff.getStatus() == 1 ? 0 : 1);
            staff.setLastModifiedDate(System.currentTimeMillis());
            return staffRepository.save(staff);
        }).orElse(null);
    }

    private void validateStaff(Staff staff, UUID currentId) {
        if (staff.getStaffCode() == null || staff.getStaffCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống");
        }
        if (staff.getAccountFpt() == null || staff.getAccountFpt().trim().isEmpty()) {
            throw new IllegalArgumentException("Email FPT không được để trống");
        }
        if (staff.getAccountFe() == null || staff.getAccountFe().trim().isEmpty()) {
            throw new IllegalArgumentException("Email FE không được để trống");
        }
        if (staff.getName() == null || staff.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        }
        if (staff.getStaffCode().length() > 15) {
            throw new IllegalArgumentException("Mã nhân viên phải nhỏ hơn 15 ký tự");
        }
        if (staff.getAccountFpt().length() > 100) {
            throw new IllegalArgumentException("Email FPT phải nhỏ hơn 100 ký tự");
        }
        if (staff.getAccountFe().length() > 100) {
            throw new IllegalArgumentException("Email FE phải nhỏ hơn 100 ký tự");
        }
        if (staff.getName().length() > 100) {
            throw new IllegalArgumentException("Tên nhân viên phải nhỏ hơn 100 ký tự");
        }
        if (!staff.getAccountFpt().endsWith("@fpt.edu.vn")) {
            throw new IllegalArgumentException("Email FPT phải kết thúc bằng @fpt.edu.vn");
        }
        if (!staff.getAccountFe().endsWith("@fe.edu.vn")) {
            throw new IllegalArgumentException("Email FE phải kết thúc bằng @fe.edu.vn");
        }
        if (staff.getAccountFpt().matches(".*\\s+.*") || !staff.getAccountFpt().matches("^[\\w.%+-]+@fpt\\.edu\\.vn$")) {
            throw new IllegalArgumentException("Email FPT không được chứa khoảng trắng và phải đúng định dạng");
        }
        if (staff.getAccountFe().matches(".*\\s+.*") || !staff.getAccountFe().matches("^[\\w.%+-]+@fe\\.edu\\.vn$")) {
            throw new IllegalArgumentException("Email FE không được chứa khoảng trắng và phải đúng định dạng");
        }

        // Kiểm tra tiếng Việt trong email
        String vietnamesePattern = ".*[àáảãạâầấẩẫậăằắẳẵặèéẻẽẹêềếểễệìíỉĩịòóỏõọôồốổỗộơờớởỡợùúủũụưừứửữựỳýỷỹỵđÀÁẢÃẠÂẦẤẨẪẬĂẰẮẲẴẶÈÉẺẼẸÊỀẾỂỄỆÌÍỈĨỊÒÓỎÕỌÔỒỐỔỖỘƠỜỚỞỠỢÙÚỦŨỤƯỪỨỬỮỰỲÝỶỸỴĐ].*";
        if (staff.getAccountFpt().matches(vietnamesePattern)) {
            throw new IllegalArgumentException("Email FPT không được chứa ký tự tiếng Việt");
        }
        if (staff.getAccountFe().matches(vietnamesePattern)) {
            throw new IllegalArgumentException("Email FE không được chứa ký tự tiếng Việt");
        }

        // Kiểm tra email phải chứa mã nhân viên
        if (!staff.getAccountFpt().contains(staff.getStaffCode().toLowerCase())) {
            throw new IllegalArgumentException("Email FPT phải chứa mã nhân viên");
        }
        if (!staff.getAccountFe().contains(staff.getStaffCode().toLowerCase())) {
            throw new IllegalArgumentException("Email FE phải chứa mã nhân viên");
        }

        if (staffRepository.findByStaffCode(staff.getStaffCode()).filter(s -> !s.getId().equals(currentId)).isPresent()) {
            throw new IllegalArgumentException("Mã nhân viên đã tồn tại");
        }
        if (staffRepository.findByAccountFpt(staff.getAccountFpt()).filter(s -> !s.getId().equals(currentId)).isPresent()) {
            throw new IllegalArgumentException("Email FPT đã tồn tại");
        }
        if (staffRepository.findByAccountFe(staff.getAccountFe()).filter(s -> !s.getId().equals(currentId)).isPresent()) {
            throw new IllegalArgumentException("Email FE đã tồn tại");
        }
    }

    public Map<String, Object> importStaffFromExcel(MultipartFile file) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int totalRecords = 0;
        int successRecords = 0;
        int failedRecords = 0;
        StringBuilder details = new StringBuilder();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            totalRecords = sheet.getPhysicalNumberOfRows() - 1; // trừ header

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    // Đọc thông tin nhân viên
                    Staff staff = new Staff();
                    staff.setStaffCode(getCellStringValue(row.getCell(0)));
                    staff.setName(getCellStringValue(row.getCell(1)));
                    staff.setAccountFpt(getCellStringValue(row.getCell(2)));
                    staff.setAccountFe(getCellStringValue(row.getCell(3)));
                    staff.setStatus(Integer.parseInt(getCellStringValue(row.getCell(4))));
                    staff.setCreatedDate(System.currentTimeMillis());
                    staff.setLastModifiedDate(System.currentTimeMillis());

                    validateStaff(staff, null);
                    Staff savedStaff = staffRepository.save(staff);

                    // Xử lý bộ môn chuyên ngành (nếu có)
                    String facilityCode = getCellStringValue(row.getCell(5));
                    String departmentCode = getCellStringValue(row.getCell(6));
                    String majorCode = getCellStringValue(row.getCell(7));
                    String positionStr = getCellStringValue(row.getCell(8));

                    if (!facilityCode.isEmpty() && !departmentCode.isEmpty() && !majorCode.isEmpty()) {
                        Facility facility = facilityRepository.findByCode(facilityCode)
                                .orElseThrow(() -> new IllegalArgumentException("Cơ sở không tồn tại: " + facilityCode));
                        Department department = departmentRepository.findByCode(departmentCode)
                                .orElseThrow(() -> new IllegalArgumentException("Bộ môn không tồn tại: " + departmentCode));
                        Major major = majorRepository.findByCode(majorCode)
                                .orElseThrow(() -> new IllegalArgumentException("Chuyên ngành không tồn tại: " + majorCode));

                        // Tìm hoặc tạo DepartmentFacility
                        DepartmentFacility departmentFacility = departmentFacilityRepository
                                .findByFacilityIdAndDepartmentId(facility.getId(), department.getId())
                                .orElseGet(() -> {
                                    DepartmentFacility df = new DepartmentFacility();
                                    df.setId(UUID.randomUUID());
                                    df.setFacility(facility);
                                    df.setDepartment(department);
                                    df.setStatus(1);
                                    df.setCreatedDate(System.currentTimeMillis());
                                    df.setLastModifiedDate(System.currentTimeMillis());
                                    return departmentFacilityRepository.save(df);
                                });

// Tìm hoặc tạo mới MajorFacility
                        MajorFacility majorFacility = majorFacilityRepository
                                .findByMajorIdAndDepartmentFacilityId(major.getId(), departmentFacility.getId())
                                .orElseGet(() -> {
                                    MajorFacility mf = new MajorFacility();
                                    mf.setId(UUID.randomUUID());
                                    mf.setMajor(major);
                                    mf.setDepartmentFacility(departmentFacility);
                                    mf.setStatus(1);
                                    mf.setCreatedDate(System.currentTimeMillis());
                                    mf.setLastModifiedDate(System.currentTimeMillis());
                                    return majorFacilityRepository.save(mf);
                                });

                        // Tạo StaffMajorFacility
                        StaffMajorFacility staffMajorFacility = new StaffMajorFacility();
                        staffMajorFacility.setId(UUID.randomUUID());
                        staffMajorFacility.setStaff(savedStaff);
                        staffMajorFacility.setMajorFacility(majorFacility);
                        staffMajorFacility.setStatus(positionStr.isEmpty() ? 1 : Integer.parseInt(positionStr));
                        staffMajorFacility.setCreatedDate(System.currentTimeMillis());
                        staffMajorFacility.setLastModifiedDate(System.currentTimeMillis());

                        staffMajorFacilityRepository.save(staffMajorFacility);
                    }

                    successRecords++;
                    details.append("Dòng ").append(i + 1).append(": Thành công\n");
                } catch (Exception e) {
                    failedRecords++;
                    String errorMsg = "Dòng " + (i + 1) + ": " + e.getMessage();
                    errors.add(errorMsg);
                    details.append(errorMsg).append("\n");
                }
            }
        }

        // Lưu lịch sử import
        ImportHistory importHistory = new ImportHistory();
        importHistory.setId(UUID.randomUUID());
        importHistory.setImportDate(System.currentTimeMillis());
        importHistory.setFileName(file.getOriginalFilename());
        importHistory.setTotalRecords(totalRecords);
        importHistory.setSuccessRecords(successRecords);
        importHistory.setFailedRecords(failedRecords);
        importHistory.setDetails(details.toString());

        importHistoryRepository.save(importHistory);

        result.put("totalRecords", totalRecords);
        result.put("successRecords", successRecords);
        result.put("failedRecords", failedRecords);
        result.put("errors", errors);

        return result;
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else {
            return "";
        }
    }
}