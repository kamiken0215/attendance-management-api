package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.Roles;
import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceClass;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.model.CrudResponse;
import com.kentarokamiyama.attendancemanagementapi.service.AttendanceClassService;
import com.kentarokamiyama.attendancemanagementapi.service.UserService;
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
    private UserService userService;
    @Autowired
    private AttendanceClassService attendanceClassService;

    @GetMapping({"/companies/{companyId}/classes","/companies/{companyId}/classes/{attendanceClassCode}"})
    public AttendanceClassResponse find (HttpServletRequest request,HttpServletResponse response,
                                       @PathVariable(value = "companyId") Integer companyId,
                                       @PathVariable(value = "attendanceClassCode",required = false) String attendanceClassCode) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        if (!(email.length() > 0)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return AttendanceClassResponse.builder().error("不正なトークンです").build();
        }

        //  アクセスしてきたユーザーがuriに含まれるcompanyIdに所属しているかチェック
        User loginUser = User.builder()
                .companyId(companyId)
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return AttendanceClassResponse.builder().error("不正なユーザー").build();
        }

        if (!(Integer.parseInt(authUser.getRoleCode().replaceFirst("^0+", "")) >= Roles.ATTENDANCE_ONLY_READ)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return AttendanceClassResponse.builder().error("権限がありません").build();
        }

        AttendanceClass attendanceClass = AttendanceClass.builder()
                .companyId(companyId)
                .attendanceClassCode(attendanceClassCode)
                .build();

        List<AttendanceClass> attendanceClasses = attendanceClassService.find(attendanceClass);

        if (attendanceClasses.size() == 0) {
            return AttendanceClassResponse.builder().error("データがありません").build();
        }

        return AttendanceClassResponse.builder().attendanceClasses(attendanceClasses).build();
    }

    @GetMapping("/admin/company/attendance-class")
    public List<AttendanceClass> find (@RequestBody AttendanceClassRequest attendanceClassRequest) {
        //return attendanceClassService.find(attendanceClassRequest);
        return null;
    }

    @PostMapping("/classes")
    public CrudResponse save (HttpServletRequest request, HttpServletResponse response, @RequestBody AttendanceClassRequest attendanceClassRequest) {

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
                .companyId(attendanceClassRequest.getCompanyId())
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

        if (!(Integer.parseInt(authUser.getRoleCode().replaceFirst("^0+", "")) >= Roles.COMPANY_READ_WRITE)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .number(0)
                    .message("権限がありません")
                    .ok(false)
                    .build();
        }

        List<AttendanceClass> attendanceClasses = attendanceClassRequest.getAttendanceClasses()
                .stream()
                .filter(attendanceClass -> attendanceClass.getCompanyId() != null)
                .filter(attendanceClass -> attendanceClass.getAttendanceClassCode() != null)
                .filter(attendanceClass -> attendanceClass.getAttendanceClassName() != null)
                .filter(attendanceClass -> attendanceClass.getStartTime() != null)
                .filter(attendanceClass -> attendanceClass.getEndTime() != null)
                .collect(Collectors.toList());

        if (attendanceClasses.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return CrudResponse.builder()
                    .number(0)
                    .message("追加可能なデータがありません")
                    .ok(false)
                    .build();
        }

        int savedCount = 0;
        for (AttendanceClass a : attendanceClasses) {
            Object result = attendanceClassService.save(a);
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
                .message(savedCount + "件")
                .ok(true)
                .build();
    }

    @DeleteMapping({"/companies/{companyId}/classes","/companies/{companyId}/classes/{attendanceClassCode}"})
    public CrudResponse delete (HttpServletRequest request,HttpServletResponse response,
                          @PathVariable(value = "companyId") Integer companyId,
                          @PathVariable(value = "attendanceClassCode",required = false) String attendanceClassCode) {

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

        if (!(Integer.parseInt(authUser.getRoleCode().replaceFirst("^0+", "")) >= Roles.COMPANY_READ_WRITE)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .number(0)
                    .message("権限がありません")
                    .ok(false)
                    .build();
        }

        AttendanceClass attendanceClass = AttendanceClass.builder()
                .companyId(companyId)
                .attendanceClassCode(attendanceClassCode)
                .build();
        List<AttendanceClass> attendanceClasses = attendanceClassService.find(attendanceClass);

        if (attendanceClasses.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return CrudResponse.builder()
                    .number(0)
                    .message("削除可能なデータはありません")
                    .ok(false)
                    .build();
        }

        int deletedCount = 0;
        for (AttendanceClass a : attendanceClasses) {
            String deleteRet = attendanceClassService.delete(a);
            deletedCount ++;
            if (deleteRet.length() > 0) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return CrudResponse.builder()
                        .number(deletedCount)
                        .message(deleteRet)
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

    @DeleteMapping("/admin/company/attendance-class")
    public String delete (HttpServletResponse response, @RequestBody AttendanceClassRequest attendanceClassRequest) {

        //List<AttendanceClass> attendanceClasses = attendanceClassService.find(attendanceClassRequest);
        //attendanceClassService.delete(attendanceClasses);
        return "";
    }


}
