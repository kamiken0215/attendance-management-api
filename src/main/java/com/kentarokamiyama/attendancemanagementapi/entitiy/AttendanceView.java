package com.kentarokamiyama.attendancemanagementapi.entitiy;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "view_attendance")
@Data
public class AttendanceView {

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "department_code")
    private String departmentCode;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

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

    @Column(name = "attendance_class_name")
    private String attendanceClassName;


    @Column(name = "attendance_status_code")
    private String attendanceStatusCode;

    @Column(name = "attendance_status_name")
    private String attendanceStatusName;
}
