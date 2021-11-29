package com.kentarokamiyama.attendancemanagementapi.controller.response;

import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceClassResponse {

    private List<AttendanceClass> attendanceClasses;

    private String error;
}
