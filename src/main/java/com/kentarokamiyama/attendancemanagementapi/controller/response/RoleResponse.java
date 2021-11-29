package com.kentarokamiyama.attendancemanagementapi.controller.response;

import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceStatus;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private List<Role> roles;

    private String error;
}
