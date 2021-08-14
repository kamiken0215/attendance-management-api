package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import com.kentarokamiyama.attendancemanagementapi.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class AttendanceController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/attendance")
    public List<Attendance> find (HttpServletRequest request,@RequestBody AttendanceRequest attendanceRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if (attendanceService.isNotExistUser(attendanceRequest.getUserId(), loginUser)) {
            return null;
        }
        return attendanceService.find(attendanceRequest);
    }

    @GetMapping("admin/attendance")
    public List<Attendance> find (@RequestBody AttendanceRequest attendanceRequest) {
        return attendanceService.find(attendanceRequest);
    }

    @PostMapping("/attendance")
    public Attendance saveAttendance (HttpServletRequest request,@RequestBody AttendanceRequest attendanceRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if (attendanceService.isNotExistUser(attendanceRequest.getUserId(), loginUser)) {
            return null;
        }
        Attendance attendance = new Attendance();
        attendance.setUserId(attendanceRequest.getUserId());
        attendance.setAttendanceDate(attendanceRequest.getAttendanceDate());
        attendance.setStartTime(attendanceRequest.getStartTime());
        attendance.setEndTime(attendanceRequest.getEndTime());
        attendance.setAttendanceClassCode(attendanceRequest.getAttendanceClassCode());
        attendance.setAttendanceStatusCode(attendanceRequest.getAttendanceStatusCode());
        return attendanceService.save(attendance);
    }

    @PostMapping("admin/attendance")
    public Attendance saveAttendance (@RequestBody AttendanceRequest attendanceRequest) {
        Attendance attendance = new Attendance();
        attendance.setUserId(attendanceRequest.getUserId());
        attendance.setAttendanceDate(attendanceRequest.getAttendanceDate());
        attendance.setStartTime(attendanceRequest.getStartTime());
        attendance.setEndTime(attendanceRequest.getEndTime());
        attendance.setAttendanceClassCode(attendanceRequest.getAttendanceClassCode());
        attendance.setAttendanceStatusCode(attendanceRequest.getAttendanceStatusCode());
        return attendanceService.save(attendance);
    }

    @DeleteMapping("/attendance")
    public void deleteAttendance (HttpServletRequest request,HttpServletResponse response, @RequestBody AttendanceRequest attendanceRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if (attendanceService.isNotExistUser(attendanceRequest.getUserId(), loginUser)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        Attendance attendance = new Attendance();
        attendance.setUserId(attendanceRequest.getUserId());
        attendance.setAttendanceDate(attendanceRequest.getAttendanceDate());
        attendanceService.delete(attendance);
    }

    @DeleteMapping("admin/attendance")
    public void deleteAttendance (@RequestBody AttendanceRequest attendanceRequest) {
        Attendance attendance = new Attendance();
        attendance.setUserId(attendanceRequest.getUserId());
        attendance.setAttendanceDate(attendanceRequest.getAttendanceDate());
        attendanceService.delete(attendance);
    }
}
