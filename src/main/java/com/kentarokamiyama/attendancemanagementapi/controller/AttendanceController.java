package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.Roles;
import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceView;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.model.CrudResponse;
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
import java.util.stream.Collectors;

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

        if (!(email.length() > 0)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            List<AttendanceResponse> ret = new ArrayList<>();
            AttendanceResponse a = AttendanceResponse.builder().error("不正なトークンです").build();
            ret.add(a);
            return ret;
        }

        //  アクセスしてきたユーザーがuriに含まれるcompanyIdに所属しているかチェック
        User loginUser = User.builder()
                .companyId(companyId)
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            List<AttendanceResponse> ret = new ArrayList<>();
            AttendanceResponse a = AttendanceResponse.builder().error("不正なユーザーです").build();
            ret.add(a);
            return ret;
        }

        if (!(Integer.parseInt(authUser.getRoleCode().replaceFirst("^0+", "")) >= Roles.ATTENDANCE_ONLY_READ)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            List<AttendanceResponse> ret = new ArrayList<>();
            AttendanceResponse a = AttendanceResponse.builder().error("権限がありません").build();
            ret.add(a);
            return ret;
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

            if (attendances.size() > 0) {
                AttendanceResponse resp = AttendanceResponse.builder()
                        .userId(attendances.get(0).getUserId())
                        .attendances(attendances)
                        .error("")
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

            if (attendances.size() > 0) {
                AttendanceResponse resp = AttendanceResponse.builder()
                        .userId(attendances.get(0).getUserId())
                        .attendances(attendances)
                        .error("")
                        .build();
                attendanceResponses.add(resp);
            }
        }

        return attendanceResponses;
    }

    @PostMapping("/attendances")
    public CrudResponse saveAttendance (HttpServletRequest request, HttpServletResponse response, @RequestBody AttendanceRequest attendanceRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        if (!(email.length() > 0)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .number(0)
                    .message("不正なトークンです")
                    .ok(false)
                    .build();
        }

        //  アクセスしてきたユーザーがuriに含まれるcompanyIdに所属しているかチェック
        User loginUser = User.builder()
                .companyId(attendanceRequest.getCompanyId())
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .number(0)
                    .message("不正なユーザーです")
                    .ok(false)
                    .build();
        }

        if (!(Integer.parseInt(authUser.getRoleCode().replaceFirst("^0+", "")) >= Roles.ATTENDANCE_READ_WRITE)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .number(0)
                    .message("権限がありません")
                    .ok(false)
                    .build();
        }

        List<Attendance> attendances = attendanceRequest.getAttendances()
                .stream()
                .filter(attendance -> attendance.getUserId() != null)
                .filter(attendance -> attendance.getAttendanceDate() != null && attendance.getAttendanceDate().matches("\\d{4}\\d{2}\\d{2}"))
                .filter(attendance -> attendance.getStartTime() != null)
                .filter(attendance -> attendance.getEndTime() != null)
                .filter(attendance -> attendance.getAttendanceClassCode() != null)
                .filter(attendance -> attendance.getAttendanceStatusCode() != null)
                .collect(Collectors.toList());

        if (attendances.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return CrudResponse.builder()
                    .number(0)
                    .message("追加可能なデータはありません")
                    .ok(false)
                    .build();
        }

        int savedCount = 0;
        for (Attendance a : attendances) {
            Object result = attendanceService.save(a);
            if (result instanceof String) {
                return CrudResponse.builder()
                        .number(savedCount)
                        .message(result.toString())
                        .ok(false)
                        .build();
            }
            savedCount ++;
        }
        return CrudResponse.builder()
                .number(savedCount)
                .message(savedCount + "件完了")
                .ok(true)
                .build();
    }

    @DeleteMapping({"companies/{companyId}/attendances",
            "companies/{companyId}/departments/{departmentCode}/attendances",
            "companies/{companyId}/departments/{departmentCode}/users/{userId}/attendances",
            "companies/{companyId}/departments/{departmentCode}/users/{userId}/attendances/{attendanceDate}"})
    public CrudResponse deleteAttendance (HttpServletRequest request,HttpServletResponse response,
                                  @PathVariable(value = "companyId") Integer companyId,
                                  @PathVariable(value = "departmentCode",required = false) String departmentCode,
                                  @PathVariable(value = "userId",required = false) Integer userId,
                                  @PathVariable(value = "attendanceDate",required = false) String attendanceDate) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        if (!(email.length() > 0)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .number(0)
                    .message("不正なトークンです")
                    .ok(false)
                    .build();
        }

        //  アクセスしてきたユーザーがuriに含まれるcompanyIdに所属しているかチェック
        User loginUser = User.builder()
                .companyId(companyId)
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .number(0)
                    .message("不正なユーザーです")
                    .ok(false)
                    .build();
        }

        if (!(Integer.parseInt(authUser.getRoleCode().replaceFirst("^0+", "")) >= Roles.ATTENDANCE_READ_WRITE)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .number(0)
                    .message("権限がありません")
                    .ok(false)
                    .build();
        }

        if (attendanceDate != null) {
            AttendanceView attendance = AttendanceView.builder()
                    .companyId(companyId)
                    .departmentCode(departmentCode)
                    .userId(userId)
                    .attendanceDate(attendanceDate)
                    .build();

            List<AttendanceView> attendances = attendanceService.find(attendance);

            if (attendances.size() == 0) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return CrudResponse.builder()
                        .number(0)
                        .message("ユーザーが見つかりません")
                        .ok(false)
                        .build();
            }

            int deletedCount = 0;

            for (AttendanceView attendanceView : attendances) {
                Attendance a = Attendance.builder()
                        .userId(attendanceView.getUserId())
                        .attendanceDate(attendanceView.getAttendanceDate())
                        .startTime(attendanceView.getStartTime())
                        .endTime(attendanceView.getEndTime())
                        .attendanceClassCode(attendanceView.getAttendanceClassCode())
                        .attendanceStatusCode(attendanceView.getAttendanceStatusCode())
                        .build();
                String deleteRet = attendanceService.delete(a);
                deletedCount ++;
                if (deleteRet.length() > 0) {
                    response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
                    return CrudResponse.builder()
                            .number(deletedCount)
                            .message(deletedCount + "件目エラー")
                            .ok(false)
                            .build();
                }
            }
            return CrudResponse.builder()
                    .number(deletedCount)
                    .message(deletedCount + "件削除")
                    .ok(true)
                    .build();
        }

        User user = User.builder()
                .companyId(companyId)
                .departmentCode(departmentCode)
                .userId(userId)
                .build();

        List<User> users = userService.find(user);
        if (users.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return CrudResponse.builder()
                    .number(0)
                    .message("ユーザーが見つかりません")
                    .ok(false)
                    .build();
        }

        int deletedCount = 0;
        for (User u : users) {
            AttendanceView attendance = AttendanceView.builder()
                    .companyId(companyId)
                    .departmentCode(departmentCode)
                    .userId(u.getUserId())
                    .build();

            List<AttendanceView> attendances = attendanceService.find(attendance);

            for (AttendanceView attendanceView : attendances) {
                Attendance a = Attendance.builder()
                        .userId(attendanceView.getUserId())
                        .attendanceDate(attendanceView.getAttendanceDate())
                        .startTime(attendanceView.getStartTime())
                        .endTime(attendanceView.getEndTime())
                        .attendanceClassCode(attendanceView.getAttendanceClassCode())
                        .attendanceStatusCode(attendanceView.getAttendanceStatusCode())
                        .build();
                String deleteRet = attendanceService.delete(a);
                deletedCount ++;
                if (deleteRet.length() > 0) {
                    response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
                    return CrudResponse.builder()
                            .number(deletedCount)
                            .message(deletedCount + "件目エラー")
                            .ok(false)
                            .build();
                }
            }
        }
        return CrudResponse.builder()
                .number(deletedCount)
                .message(deletedCount + "件削除")
                .ok(true)
                .build();
    }
}
