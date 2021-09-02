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
@Table(name = "attendance_class")
@Data
@IdClass(AttendanceClassPK.class)
public class AttendanceClass {

    @Id
    @Column(name = "attendance_class_code")
    private String attendanceClassCode;

    @Column(name = "attendance_class_name")
    private String attendanceClassName;

    @ManyToOne
    @JoinColumn(name = "company_id",referencedColumnName = "company_id", insertable = false, updatable = false)
    private Company company;

    @Id
    @Column(name = "company_id")
    private Integer companyId;

}
