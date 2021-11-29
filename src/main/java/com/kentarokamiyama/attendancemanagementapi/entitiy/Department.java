package com.kentarokamiyama.attendancemanagementapi.entitiy;

import com.kentarokamiyama.attendancemanagementapi.entitiy.pk.DepartmentPK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "department")
@Data
@IdClass(DepartmentPK.class)
public class Department {

    @Id
    @Column(name = "company_id")
    private Integer companyId;

    @Id
    @Column(name = "department_code")
    private String departmentCode;

    @Column(name = "department_name")
    private String departmentName;

}
