package com.kentarokamiyama.attendancemanagementapi.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private Integer userId;

    private String userName;

    private String email;

    private String password;

    private Integer paidHolidays;

    private String isActive;

    @NotEmpty
    private Integer companyId;

    private String departmentCode;

    private String roleCode;
}
