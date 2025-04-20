package com.example.employeesmanager.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {

    @Id
    @GeneratedValue(generator = "uuid2")
    @org.hibernate.annotations.GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "account_fpt", length = 100, nullable = false, unique = true)
    private String accountFpt;

    @Column(name = "account_fe", length = 100, nullable = false, unique = true)
    private String accountFe;

    @Column(name = "staff_code", length = 15, nullable = false, unique = true)
    private String staffCode;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "status")
    private Integer status;

    @Column(name = "created_date")
    private Long createdDate;

    @Column(name = "last_modified_date")
    private Long lastModifiedDate;
}
