package com.kentarokamiyama.attendancemanagementapi.entitiy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance_status")
@Data
public class AttendanceStatus {

    @Id
    @Column(name = "attendance_status_code")
    private String attendanceStatusCode;

    @Column(name = "attendance_status_name")
    private String attendanceStatusName;

}
