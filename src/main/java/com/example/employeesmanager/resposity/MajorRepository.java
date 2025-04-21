package com.example.employeesmanager.resposity;

import com.example.employeesmanager.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MajorRepository extends JpaRepository<Major, UUID> {
    Optional<Major> findByCode(String code);
}

