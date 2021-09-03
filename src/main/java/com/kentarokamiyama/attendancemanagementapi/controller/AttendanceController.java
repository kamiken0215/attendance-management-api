package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceView;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.service.AttendanceService;
import com.kentarokamiyama.attendancemanagementapi.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@Log
public class AttendanceController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping({"companies/{companyId}/attendances",
            "companies/{companyId}/departments/{departmentCode}/attendances",
            "companies/{companyId}/departments/{departmentCode}/users/{userId}/attendances",
            "companies/{companyId}/departments/{departmentCode}/users/{userId}/attendances/{attendanceDate}"})
    public List<AttendanceResponse> find (HttpServletRequest request,HttpServletResponse response,
                                  @PathVariable(value = "companyId") Integer companyId,
                                  @PathVariable(value = "departmentCode",required = false) String departmentCode,
                                  @PathVariable(value = "userId",required = false) Integer userId,
                                  @PathVariable(value = "attendanceDate",required = false) String attendanceDate) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        //  アクセスしてきたユーザーがuriに含まれるcompanyIdに所属しているかチェック
        User loginUser = User.builder()
                .companyId(companyId)
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        //  単一ユーザーの場合
        if (attendanceDate != null) {
            AttendanceView attendance = AttendanceView.builder()
                    .companyId(companyId)
                    .departmentCode(departmentCode)
                    .userId(userId)
                    .attendanceDate(attendanceDate)
                    .build();

            LinkedList<AttendanceResponse> attendanceResponses = new LinkedList<>();
            List<AttendanceView> attendances = attendanceService.find(attendance);
            for (AttendanceView a : attendances) {
                AttendanceResponse resp = AttendanceResponse.builder()
                        .userId(a.getUserId())
                        .userName(a.getUserName())
                        .attendanceDate(a.getAttendanceDate())
                        .startTime(a.getStartTime())
                        .endTime(a.getEndTime())
                        .attendanceClassCode(a.getAttendanceClassCode())
                        .attendanceClassName(a.getAttendanceClassName())
                        .attendanceStatusCode(a.getAttendanceStatusCode())
                        .attendanceStatusName(a.getAttendanceStatusName())
                        .build();
                attendanceResponses.add(resp);
            }
            return attendanceResponses;
        }

        User filterUser =  User.builder()
                .companyId(companyId)
                .departmentCode(departmentCode)
                .userId(userId)
                .build();

        LinkedList<AttendanceResponse> attendanceResponses = new LinkedList<>();
        List<User> users = userService.find(filterUser);

        //  各ユーザーの出勤データを取得
        for (User u : users) {
            AttendanceView attendance = AttendanceView.builder()
                    .companyId(u.getCompanyId())
                    .departmentCode(u.getDepartmentCode())
                    .userId(u.getUserId())
                    .build();
            List<AttendanceView> attendances = attendanceService.find(attendance);
            for (AttendanceView a : attendances) {
                AttendanceResponse resp = AttendanceResponse.builder()
                        .userId(a.getUserId())
                        .userName(a.getUserName())
                        .attendanceDate(a.getAttendanceDate())
                        .startTime(a.getStartTime())
                        .endTime(a.getEndTime())
                        .attendanceClassCode(a.getAttendanceClassCode())
                        .attendanceClassName(a.getAttendanceClassName())
                        .attendanceStatusCode(a.getAttendanceStatusCode())
                        .attendanceStatusName(a.getAttendanceStatusName())
                        .build();
                attendanceResponses.add(resp);
            }
            attendances.clear();
        }

        return attendanceResponses;
    }

    @GetMapping("admin/attendance")
    public List<AttendanceView> find (@RequestBody AttendanceRequest attendanceRequest) {
        AttendanceView attendance = AttendanceView.builder()
                .userId(attendanceRequest.getUserId())
                .attendanceDate(attendanceRequest.getAttendanceDate())
                .attendanceClassCode(attendanceRequest.getAttendanceClassCode())
                .attendanceStatusCode(attendanceRequest.getAttendanceStatusCode())
                .build();
        return attendanceService.find(attendance);
    }

    @PostMapping("/attendances")
    public AttendanceResponse saveAttendance (HttpServletRequest request,HttpServletResponse response,@RequestBody AttendanceRequest attendanceRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        //  アクセスしてきたユーザーがuriに含まれるcompanyIdに所属しているかチェック
        User loginUser = User.builder()
                .companyId(attendanceRequest.getCompanyId())
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        //  本人確認
        if (attendanceRequest.getUserId() != null) {
            if (!attendanceRequest.getUserId().equals(authUser.getUserId())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
        }

        Attendance attendance = Attendance.builder()
                .userId(attendanceRequest.getUserId())
                .attendanceDate(attendanceRequest.getAttendanceDate())
                .startTime(attendanceRequest.getStartTime())
                .endTime(attendanceRequest.getEndTime())
                .attendanceClassCode(attendanceRequest.getAttendanceClassCode())
                .attendanceStatusCode(attendanceRequest.getAttendanceStatusCode())
                .build();

        Object result = attendanceService.save(attendance);

        if (result instanceof Attendance) {
            Attendance a = (Attendance) result;
            return AttendanceResponse.builder()
                    .userId(a.getUserId())
                    .attendanceDate(a.getAttendanceDate())
                    .startTime(a.getStartTime())
                    .endTime(a.getEndTime())
                    .attendanceClassCode(a.getAttendanceClassCode())
                    .attendanceStatusCode(a.getAttendanceStatusCode())
                    .build();
        } else {
            return AttendanceResponse.builder().error(result.toString()).build();
        }

    }

    @PostMapping("admin/attendance")
    public AttendanceResponse saveAttendance (@RequestBody AttendanceRequest attendanceRequest) {
        Attendance attendance = new Attendance();
        attendance.setUserId(attendanceRequest.getUserId());
        attendance.setAttendanceDate(attendanceRequest.getAttendanceDate());
        attendance.setStartTime(attendanceRequest.getStartTime());
        attendance.setEndTime(attendanceRequest.getEndTime());
        attendance.setAttendanceClassCode(attendanceRequest.getAttendanceClassCode());
        attendance.setAttendanceStatusCode(attendanceRequest.getAttendanceStatusCode());

        Object result = attendanceService.save(attendance);

        if (result instanceof Attendance) {
            Attendance a = (Attendance) result;
            return AttendanceResponse.builder()
                    .userId(a.getUserId())
                    .attendanceDate(a.getAttendanceDate())
                    .startTime(a.getStartTime())
                    .endTime(a.getEndTime())
                    .attendanceClassCode(a.getAttendanceClassCode())
                    .attendanceStatusCode(a.getAttendanceStatusCode())
                    .build();
        } else {
            return AttendanceResponse.builder().error(result.toString()).build();
        }
    }

    @DeleteMapping({"companies/{companyId}/attendances",
            "companies/{companyId}/departments/{departmentCode}/attendances",
            "companies/{companyId}/departments/{departmentCode}/users/{userId}/attendances",
            "companies/{companyId}/departments/{departmentCode}/users/{userId}/attendances/{attendanceDate}"})
    public String deleteAttendance (HttpServletRequest request,HttpServletResponse response,
                                  @PathVariable(value = "companyId") Integer companyId,
                                  @PathVariable(value = "departmentCode",required = false) String departmentCode,
                                  @PathVariable(value = "userId",required = false) Integer userId,
                                  @PathVariable(value = "attendanceDate",required = false) String attendanceDate) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        //  アクセスしてきたユーザーがuriに含まれるcompanyIdに所属しているかチェック
        User loginUser = User.builder()
                .companyId(companyId)
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "不正";
        }

        if (attendanceDate != null) {
            AttendanceView attendance = AttendanceView.builder()
                    .userId(userId)
                    .attendanceDate(attendanceDate)
                    .build();
            List<AttendanceView> attendances = attendanceService.find(attendance);

            //  !!must create attendance list
            //attendanceService.deleteAll(attendances);
            return attendanceDate +" 削除";
        }

        User user = User.builder()
                .companyId(companyId)
                .departmentCode(departmentCode)
                .userId(userId)
                .build();

        List<User> users = userService.find(user);

        for (User u : users) {
            AttendanceView attendance = AttendanceView.builder()
                    .userId(u.getUserId())
                    .build();

            List<AttendanceView> attendances = attendanceService.find(attendance);

            //  !!!must
            //attendanceService.deleteAll(attendances);
        }

        return companyId + ":" + departmentCode + ":" + userId + "削除";

    }

    @DeleteMapping("admin/attendance")
    public void deleteAttendance (@RequestBody AttendanceRequest attendanceRequest) {
//        Attendance attendance = new Attendance();
//        attendance.setUserId(attendanceRequest.getUserId());
//        attendance.setAttendanceDate(attendanceRequest.getAttendanceDate());
//        List<Attendance> attendances = attendanceService.find(attendance);
//        attendanceService.deleteAll(attendances);
    }
}
