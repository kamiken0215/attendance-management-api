package com.kentarokamiyama.attendancemanagementapi.controller.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AuthRequest {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
