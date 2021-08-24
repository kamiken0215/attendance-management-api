package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.service.AttendanceService;
import com.kentarokamiyama.attendancemanagementapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
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
            Attendance attendance = Attendance.builder()
                    .userId(userId)
                    .attendanceDate(attendanceDate)
                    .build();

            List<AttendanceResponse> attendanceResponses = new ArrayList<>();
            List<Attendance> attendances = attendanceService.find(attendance);
            for (Attendance a : attendances) {
                AttendanceResponse resp = AttendanceResponse.builder()
                        .userId(a.getUserId())
                        .userName(a.getUser().getUserName())
                        .attendanceDate(a.getAttendanceDate())
                        .startTime(a.getStartTime())
                        .endTime(a.getEndTime())
                        .attendanceClassCode(a.getAttendanceClassCode())
                        .attendanceClassName(a.getAttendanceClass().getAttendanceClassName())
                        .attendanceStatusCode(a.getAttendanceStatusCode())
                        .attendanceStatusName(a.getAttendanceStatus().getAttendanceStatusName())
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

        List<AttendanceResponse> attendanceResponses = new ArrayList<>();
        List<User> users = userService.find(filterUser);

        //  各ユーザーの出勤データを取得
        for (User u : users) {
            Attendance attendance = Attendance.builder()
                    .userId(u.getUserId())
                    .build();
            List<Attendance> attendances = attendanceService.find(attendance);
            for (Attendance a : attendances) {
                AttendanceResponse resp = AttendanceResponse.builder()
                        .userId(a.getUserId())
                        .userName(a.getUser().getUserName())
                        .attendanceDate(a.getAttendanceDate())
                        .startTime(a.getStartTime())
                        .endTime(a.getEndTime())
                        .attendanceClassCode(a.getAttendanceClassCode())
                        .attendanceClassName(a.getAttendanceClass().getAttendanceClassName())
                        .attendanceStatusCode(a.getAttendanceStatusCode())
                        .attendanceStatusName(a.getAttendanceStatus().getAttendanceStatusName())
                        .build();
                attendanceResponses.add(resp);
            }
            attendances.clear();
        }

        return attendanceResponses;
    }

    @GetMapping("admin/attendance")
    public List<Attendance> find (@RequestBody AttendanceRequest attendanceRequest) {
        Attendance attendance = Attendance.builder()
                .userId(attendanceRequest.getUserId())
                .attendanceDate(attendanceRequest.getAttendanceDate())
                .attendanceClassCode(attendanceRequest.getAttendanceClassCode())
                .attendanceStatusCode(attendanceRequest.getAttendanceStatusCode())
                .build();
        return attendanceService.find(attendance);
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
            Attendance attendance = Attendance.builder()
                    .userId(userId)
                    .attendanceDate(attendanceDate)
                    .build();
            List<Attendance> attendances = attendanceService.find(attendance);
            attendanceService.deleteAll(attendances);
            return attendanceDate +" 削除";
        }

        User user = User.builder()
                .companyId(companyId)
                .departmentCode(departmentCode)
                .userId(userId)
                .build();

        List<User> users = userService.find(user);

        for (User u : users) {
            Attendance attendance = Attendance.builder()
                    .userId(u.getUserId())
                    .build();

            List<Attendance> attendances = attendanceService.find(attendance);
            attendanceService.deleteAll(attendances);
        }

        return companyId + ":" + departmentCode + ":" + userId + "削除";

    }

    @DeleteMapping("admin/attendance")
    public void deleteAttendance (@RequestBody AttendanceRequest attendanceRequest) {
        Attendance attendance = new Attendance();
        attendance.setUserId(attendanceRequest.getUserId());
        attendance.setAttendanceDate(attendanceRequest.getAttendanceDate());
        List<Attendance> attendances = attendanceService.find(attendance);
        attendanceService.deleteAll(attendances);
    }
}
