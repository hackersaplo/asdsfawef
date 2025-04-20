package com.example.employeesmanager.resposity;

import com.example.employeesmanager.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface FacilityRepository extends JpaRepository<Facility, UUID> {}
