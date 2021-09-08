package com.kentarokamiyama.attendancemanagementapi.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequest {

    @NotEmpty
    private Integer CompanyId;

    private List<Attendance> attendances;

}
