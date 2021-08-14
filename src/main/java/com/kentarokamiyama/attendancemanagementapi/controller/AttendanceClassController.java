package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceClass;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Department;
import com.kentarokamiyama.attendancemanagementapi.repository.AttendanceClassRepository;
import com.kentarokamiyama.attendancemanagementapi.service.AttendanceClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AttendanceClassController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private AttendanceClassService attendanceClassService;

    @GetMapping("/company/attendance-class")
    public List<AttendanceClass> find (HttpServletRequest request, HttpServletResponse response, @RequestBody AttendanceClassRequest attendanceClassRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if (!attendanceClassService.isCompanyUser(loginUser,attendanceClassRequest.getCompanyId())) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return attendanceClassService.find(attendanceClassRequest);
    }

    @GetMapping("/admin/company/attendance-class")
    public List<AttendanceClass> find (@RequestBody AttendanceClassRequest attendanceClassRequest) {
        return attendanceClassService.find(attendanceClassRequest);
    }

    @PostMapping("/company/attendance-class")
    public List<AttendanceClass> save (HttpServletRequest request, HttpServletResponse response, @RequestBody AttendanceClassRequest attendanceClassRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if (!attendanceClassService.isCompanyUser(loginUser,attendanceClassRequest.getCompanyId())) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        List<AttendanceClass> attendanceClasses = attendanceClassRequest.getAttendanceClasses()
                .stream()
                .filter(attendanceClass -> attendanceClass.getCompanyId() != null)
                .filter(attendanceClass -> attendanceClass.getAttendanceClassCode() != null)
                .filter(attendanceClass -> attendanceClass.getAttendanceClassName() != null)
                .collect(Collectors.toList());

        if (attendanceClasses.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return attendanceClassService.save(attendanceClasses);
    }

    @PostMapping("/admin/company/attendance-class")
    public List<AttendanceClass> save (HttpServletResponse response,@RequestBody AttendanceClassRequest attendanceClassRequest) {

        List<AttendanceClass> attendanceClasses = attendanceClassRequest.getAttendanceClasses()
                .stream()
                .filter(attendanceClass -> attendanceClass.getCompanyId() != null)
                .filter(attendanceClass -> attendanceClass.getAttendanceClassCode() != null)
                .filter(attendanceClass -> attendanceClass.getAttendanceClassName() != null)
                .collect(Collectors.toList());

        if (attendanceClasses.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return attendanceClassService.save(attendanceClasses);
    }

    @DeleteMapping("/company/attendance-class")
    public String delete (HttpServletRequest request, HttpServletResponse response, @RequestBody AttendanceClassRequest attendanceClassRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if (!attendanceClassService.isCompanyUser(loginUser,attendanceClassRequest.getCompanyId())) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        List<AttendanceClass> attendanceClasses = attendanceClassService.find(attendanceClassRequest);

        attendanceClassService.delete(attendanceClasses);
        return "";
    }

    @DeleteMapping("/admin/company/attendance-class")
    public String delete (HttpServletResponse response, @RequestBody AttendanceClassRequest attendanceClassRequest) {

        List<AttendanceClass> attendanceClasses = attendanceClassService.find(attendanceClassRequest);
        attendanceClassService.delete(attendanceClasses);
        return "";
    }


}
