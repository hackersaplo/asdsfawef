package com.example.employeesmanager.service;

import com.example.employeesmanager.entity.Major;
import com.example.employeesmanager.resposity.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MajorService {
    @Autowired
    private MajorRepository majorRepository;

    public List<Major> getAllMajors() {
        return majorRepository.findAll();
    }

    public Optional<Major> getMajorById(UUID id) {
        return majorRepository.findById(id);
    }

    public Major saveMajor(Major major) {
        return majorRepository.save(major);
    }

    public void deleteMajor(UUID id) {
        majorRepository.deleteById(id);
    }
}
