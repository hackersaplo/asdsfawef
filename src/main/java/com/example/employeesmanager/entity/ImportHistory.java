package com.example.employeesmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "import_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportHistory {
    @Id
    private UUID id;

    @Column(name = "import_date")
    private Long importDate;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "total_records")
    private Integer totalRecords;

    @Column(name = "success_records")
    private Integer successRecords;

    @Column(name = "failed_records")
    private Integer failedRecords;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details; // JSON or text describing success/failure per record
}
