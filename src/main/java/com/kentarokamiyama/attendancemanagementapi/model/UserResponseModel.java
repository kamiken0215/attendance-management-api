package com.kentarokamiyama.attendancemanagementapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseModel {
    private Integer userId;

    private String userName;

    private String email;

    private Integer paidHolidays;

    private String isActive;

    private Integer companyId;

    private String companyName;

    private String departmentCode;

    private String departmentName;

    private String roleCode;
}
