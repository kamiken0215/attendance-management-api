package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceView;
import com.kentarokamiyama.attendancemanagementapi.repository.AttendanceRepository;
import com.kentarokamiyama.attendancemanagementapi.service.AttendanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
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

        List<Attendance> attendances = new ArrayList<>();
        attendances.add(attendance);

        AttendanceView find = AttendanceView.builder()
                .userId(1)
                .attendanceDate("20200101")
                .build();

        attendanceService.save(attendance);

        List<AttendanceView> createdAttendances = attendanceService.find(find);
        assertEquals(createdAttendances.get(0).getAttendanceDate(),"20200101");
        attendanceService.deleteAll(attendances);
    }

    @Test
    void testSaveAttendance() {
    }


    @Test
    void testDeleteAttendance() {
    }
}