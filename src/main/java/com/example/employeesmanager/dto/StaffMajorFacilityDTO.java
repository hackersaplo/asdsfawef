package com.example.employeesmanager.dto;

import java.util.UUID;

public class StaffMajorFacilityDTO {
    private UUID id;
    private String position;
    private String departmentName;
    private String majorName;
    private String facilityName;

    public StaffMajorFacilityDTO(UUID id, String position, String departmentName, String majorName, String facilityName) {
        this.id = id;
        this.position = position;
        this.departmentName = departmentName;
        this.majorName = majorName;
        this.facilityName = facilityName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }
}
