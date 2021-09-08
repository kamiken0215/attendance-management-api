package com.kentarokamiyama.attendancemanagementapi.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {

    private Integer userId;

    private List<AttendanceView> attendances;

    private String error;
}
