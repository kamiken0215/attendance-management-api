package com.kentarokamiyama.attendancemanagementapi.entitiy;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "attendance_class")
@Data
public class AttendanceClass {

    @Id
    @Column(name = "attendance_class_code")
    private String attendanceClassCode;

    @Column(name = "attendance_class_name")
    private String attendanceClassName;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
