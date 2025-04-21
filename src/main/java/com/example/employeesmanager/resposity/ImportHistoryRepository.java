package com.example.employeesmanager.resposity;

import com.example.employeesmanager.entity.ImportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ImportHistoryRepository extends JpaRepository<ImportHistory, UUID> {
    List<ImportHistory> findAllByOrderByImportDateDesc();
}
