package com.example.employeesmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

public class FacilityHierarchyDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MajorDTO {
        private UUID id;
        private String code;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentDTO {
        private UUID id;
        private String code;
        private String name;
        private List<MajorDTO> majors;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacilityDTO {
        private UUID id;
        private String code;
        private String name;
        private List<DepartmentDTO> departments;
    }
}
