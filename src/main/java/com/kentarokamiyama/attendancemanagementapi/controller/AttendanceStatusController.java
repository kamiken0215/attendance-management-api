package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.Roles;
import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceStatus;
import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceView;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.model.CrudResponse;
import com.kentarokamiyama.attendancemanagementapi.service.AttendanceService;
import com.kentarokamiyama.attendancemanagementapi.service.AttendanceStatusService;
import com.kentarokamiyama.attendancemanagementapi.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Log
public class AttendanceStatusController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private AttendanceStatusService attendanceStatusService;

    @GetMapping("/status")
    public AttendanceStatusResponse find (HttpServletRequest request,HttpServletResponse response) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        if (!(email.length() > 0)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return AttendanceStatusResponse.builder().error("不正なトークンです").build();
        }


        List<AttendanceStatus> attendanceStatuses = attendanceStatusService.find();

        if (attendanceStatuses.size() == 0) {
            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            return AttendanceStatusResponse.builder().error("データが取得できませんでした").build();
        }

        return AttendanceStatusResponse.builder().attendanceStatuses(attendanceStatuses).build();

    }
}
