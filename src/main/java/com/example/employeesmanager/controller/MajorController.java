package com.example.employeesmanager.controller;

import com.example.employeesmanager.entity.Major;
import com.example.employeesmanager.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/majors")
public class MajorController {

    @Autowired
    private MajorService majorService;

    @GetMapping
    public List<Major> getAllMajors() {
        return majorService.getAllMajors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Major> getMajorById(@PathVariable UUID id) {
        Optional<Major> major = majorService.getMajorById(id);
        return major.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Major createMajor(@RequestBody Major major) {
        return majorService.saveMajor(major);
    }

    @DeleteMapping("/{id}")
    public void deleteMajor(@PathVariable UUID id) {
        majorService.deleteMajor(id);
    }
}