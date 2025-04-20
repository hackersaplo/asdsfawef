package com.example.employeesmanager.service;

import com.example.employeesmanager.entity.Staff;
import com.example.employeesmanager.resposity.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Optional<Staff> getStaffById(UUID id) {
        return staffRepository.findById(id);
    }

    // Phương thức thêm nhân viên
    public Staff addStaff(Staff staff) {
        // Kiểm tra tính hợp lệ của nhân viên (không cần UUID khi thêm mới)
        validateStaff(staff, null);
        return staffRepository.save(staff);
    }

    // Phương thức cập nhật thông tin nhân viên
    public Staff updateStaff(UUID id, Staff updatedStaff) {
        return staffRepository.findById(id).map(existing -> {
            // Kiểm tra tính hợp lệ của nhân viên khi cập nhật (cần UUID để kiểm tra sự thay đổi)
            validateStaff(updatedStaff, id);
            existing.setAccountFe(updatedStaff.getAccountFe());
            existing.setAccountFpt(updatedStaff.getAccountFpt());
            existing.setName(updatedStaff.getName());
            existing.setStaffCode(updatedStaff.getStaffCode());
            existing.setStatus(updatedStaff.getStatus());
            return staffRepository.save(existing);
        }).orElse(null);
    }

    // Phương thức xóa nhân viên
    public void deleteStaff(UUID id) {
        staffRepository.deleteById(id);
    }

    // Phương thức đổi trạng thái nhân viên
    public Staff toggleStatus(UUID id) {
        return staffRepository.findById(id).map(staff -> {
            staff.setStatus(staff.getStatus() == 1 ? 0 : 1);
            return staffRepository.save(staff);
        }).orElse(null);
    }

    // Phương thức kiểm tra tính hợp lệ của nhân viên
    private void validateStaff(Staff staff, UUID currentId) {
        // Kiểm tra các trường không được để trống
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

        // Kiểm tra độ dài các trường
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

        // Kiểm tra định dạng email và đuôi email
        if (!staff.getAccountFpt().endsWith("@fpt.edu.vn")) {
            throw new IllegalArgumentException("Email FPT phải kết thúc bằng @fpt.edu.vn");
        }
        if (!staff.getAccountFe().endsWith("@fe.edu.vn")) {
            throw new IllegalArgumentException("Email FE phải kết thúc bằng @fe.edu.vn");
        }

        // Kiểm tra email không chứa khoảng trắng và không có ký tự tiếng Việt
        if (staff.getAccountFpt().matches(".*\\s+.*") || !staff.getAccountFpt().matches("^[\\w.%+-]+@fpt\\.edu\\.vn$")) {
            throw new IllegalArgumentException("Email FPT không được chứa khoảng trắng và phải đúng định dạng");
        }
        if (staff.getAccountFe().matches(".*\\s+.*") || !staff.getAccountFe().matches("^[\\w.%+-]+@fe\\.edu\\.vn$")) {
            throw new IllegalArgumentException("Email FE không được chứa khoảng trắng và phải đúng định dạng");
        }

        // Kiểm tra tính duy nhất của mã nhân viên, email FPT và email FE
        // Giả sử staffRepository có các phương thức findByStaffCode, findByAccountFpt, findByAccountFe
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
}
