package com.kentarokamiyama.attendancemanagementapi.entitiy;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "attendance_status")
@Data
public class AttendanceStatus {

    @Id
    @Column(name = "attendance_status_code")
    private String attendanceStatusCode;

    @Column(name = "attendance_status_name")
    private String attendanceStatusName;

}
