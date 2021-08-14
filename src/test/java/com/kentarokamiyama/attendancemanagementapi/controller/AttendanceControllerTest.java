package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import com.kentarokamiyama.attendancemanagementapi.repository.AttendanceRepository;
import com.kentarokamiyama.attendancemanagementapi.service.AttendanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AttendanceControllerTest {

    @Autowired
    private AttendanceService attendanceService;

    @Test
    @Rollback
    void testFind() {
        Attendance attendance = Attendance.builder()
                .userId(1)
                .attendanceDate("20200101")
                .attendanceClassCode("101")
                .attendanceStatusCode("101")
                .startTime(new Date())
                .endTime(new Date())
                .build();

        AttendanceRequest attendanceRequest = AttendanceRequest.builder()
                .userId(1)
                .attendanceDate("20200101")
                .build();

        Attendance createdAttendance = attendanceService.save(attendance);

        List<Attendance> createdAttendances = attendanceService.find(attendanceRequest);
        assertEquals(createdAttendances.get(0).getAttendanceDate(),"20200101");
        attendanceService.delete(attendance);
    }

    @Test
    void testSaveAttendance() {
    }


    @Test
    void testDeleteAttendance() {
    }
}