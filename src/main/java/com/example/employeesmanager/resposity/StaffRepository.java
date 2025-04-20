package com.example.employeesmanager.resposity;


import com.example.employeesmanager.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, UUID> {
    Optional<Staff> findByStaffCode(String staffCode);
    Optional<Staff> findByAccountFpt(String accountFpt);
    Optional<Staff> findByAccountFe(String accountFe);
}
