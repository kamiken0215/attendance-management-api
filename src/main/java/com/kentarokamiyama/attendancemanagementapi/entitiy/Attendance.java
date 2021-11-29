package com.kentarokamiyama.attendancemanagementapi.entitiy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kentarokamiyama.attendancemanagementapi.entitiy.pk.AttendancePK;
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

    @Column(name = "attendance_class_code")
    private String attendanceClassCode;

    @Column(name = "attendance_status_code")
    private String attendanceStatusCode;

}
