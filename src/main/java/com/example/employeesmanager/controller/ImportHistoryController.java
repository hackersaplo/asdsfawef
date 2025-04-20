package com.example.employeesmanager.controller;

import com.example.employeesmanager.entity.ImportHistory;
import com.example.employeesmanager.resposity.ImportHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import-history")
@CrossOrigin(origins = "http://localhost:3000")
public class ImportHistoryController {

    @Autowired
    private ImportHistoryRepository importHistoryRepository;

    @GetMapping
    public ResponseEntity<List<ImportHistory>> getAllImportHistory() {
        List<ImportHistory> list = importHistoryRepository.findAll();
        return ResponseEntity.ok(list);
    }
}
