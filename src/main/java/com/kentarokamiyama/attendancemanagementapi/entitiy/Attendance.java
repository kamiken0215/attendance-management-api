package com.kentarokamiyama.attendancemanagementapi.entitiy;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "attendance")
@Data
public class Attendance {

    @ManyToOne
    @JoinColumn(name = "user_id",insertable = false, updatable = false)
    private User user;

    @Id
    @Column(name = "attendance_date")
    private String attendanceDate;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @ManyToOne
    @JoinColumn(name = "attendance_class_code",insertable = false, updatable = false)
    private AttendanceClass attendanceClass;

    @ManyToOne
    @JoinColumn(name = "attendance_status_code",insertable = false, updatable = false)
    private AttendanceStatus attendanceStatus;

}
