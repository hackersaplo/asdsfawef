package com.example.employeesmanager.service;

import com.example.employeesmanager.entity.MajorFacility;
import com.example.employeesmanager.resposity.MajorFacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MajorFacilityService {

    @Autowired
    private MajorFacilityRepository majorFacilityRepository;

    public List<MajorFacility> getByMajorId(UUID majorId) {
        return majorFacilityRepository.findByMajorId(majorId);
    }

    public MajorFacility save(MajorFacility entity) {
        return majorFacilityRepository.save(entity);
    }

    public void deleteById(UUID id) {
        majorFacilityRepository.deleteById(id);
    }
}
