package com.kentarokamiyama.attendancemanagementapi.entitiy;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance")
@Data
@IdClass(AttendancePK.class)
public class Attendance {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id",insertable = false, updatable = false)
    private User user;

    @Id
    @Column(name = "attendance_date")
    private String attendanceDate;

    @Column(name = "start_time", columnDefinition="DATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Column(name = "end_time", columnDefinition="DATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ManyToOne
    @JoinColumn(name = "attendance_class_code",referencedColumnName = "attendance_class_code",insertable = false, updatable = false)
    private AttendanceClass attendanceClass;

    @Column(name = "attendance_class_code")
    private String attendanceClassCode;

    @ManyToOne
    @JoinColumn(name = "attendance_status_code",referencedColumnName = "attendance_status_code",insertable = false, updatable = false)
    private AttendanceStatus attendanceStatus;

    @Column(name = "attendance_status_code")
    private String attendanceStatusCode;

}
