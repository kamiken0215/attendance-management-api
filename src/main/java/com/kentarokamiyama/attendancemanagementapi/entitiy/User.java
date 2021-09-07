package com.kentarokamiyama.attendancemanagementapi.entitiy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",updatable = false)
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

    @Nullable
    @ManyToOne
    @JoinColumn(name = "company_id",referencedColumnName = "company_id", insertable = false, updatable = false)
    private Company company;

    @Column(name = "company_id")
    private Integer companyId;

    @Nullable
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "company_id",referencedColumnName = "company_id", insertable = false, updatable = false),
            @JoinColumn(name = "department_code",referencedColumnName = "department_code", insertable = false, updatable = false)
    })
    private Department department;

    @Column(name = "department_code")
    private String departmentCode;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "role_code",referencedColumnName = "role_code", insertable = false, updatable = false)
    private Role role;

    @Column(name = "role_code")
    private String roleCode;
}

