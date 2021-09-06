package com.kentarokamiyama.attendancemanagementapi.entitiy;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class DepartmentPK implements Serializable {

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "department_code")
    private String departmentCode;
}
