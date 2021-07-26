package com.kentarokamiyama.attendancemanagementapi.entitiy;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer usesId;

    @Column(name = "user_name")
    private String userName;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private int companyId;

    @ManyToOne
    @JoinColumn(name = "department_code")
    private Department department;

    private String departmentCode;

    @ManyToOne
    @JoinColumn(name = "role_code")
    private Role role;

    private String roleCode;

    @Column
    private String email;

    @Column
    private String password;

    @Column(name = "paid_holidays")
    private Integer paidHolidays;

    @Column(name = "is_active")
    private String isActive;
}
