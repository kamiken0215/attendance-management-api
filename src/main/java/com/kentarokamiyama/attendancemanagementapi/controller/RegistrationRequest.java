package com.kentarokamiyama.attendancemanagementapi.controller;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;


@Data
public class RegistrationRequest {
    @NotEmpty
    private String userName;
    @NotEmpty
    private Integer companyId;
    @NotEmpty
    private String departmentCode;
    @NotEmpty
    private String roleCode;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private int paidHolidays;
    @NotEmpty
    private String isActive;
}
