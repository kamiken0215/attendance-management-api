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
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "paid_holidays")
    private Integer paidHolidays;

    @Column(name = "is_active")
    private String isActive;

    @ManyToOne
    @JoinColumn(name = "company_id",referencedColumnName = "company_id", insertable = false, updatable = false)
    private Company company;

    @Column(name = "company_id")
    private Integer companyId;

    @ManyToOne
    @JoinColumn(name = "department_code",referencedColumnName = "department_code", insertable = false, updatable = false)
    private Department department;

    @Column(name = "department_code")
    private String departmentCode;

    @ManyToOne
    @JoinColumn(name = "role_code",referencedColumnName = "role_code", insertable = false, updatable = false)
    private Role role;

    @Column(name = "role_code")
    private String roleCode;


}
