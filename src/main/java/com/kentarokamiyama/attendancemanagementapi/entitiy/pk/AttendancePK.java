package com.kentarokamiyama.attendancemanagementapi.entitiy.pk;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class AttendancePK implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "attendance_date")
    private String attendanceDate;
}
