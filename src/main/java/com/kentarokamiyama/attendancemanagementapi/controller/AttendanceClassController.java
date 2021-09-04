package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceClass;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Department;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.repository.AttendanceClassRepository;
import com.kentarokamiyama.attendancemanagementapi.service.AttendanceClassService;
import com.kentarokamiyama.attendancemanagementapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
    public List<AttendanceClass> find (HttpServletRequest request,HttpServletResponse response,
                                       @PathVariable(value = "companyId") Integer companyId,
                                       @PathVariable(value = "attendanceClassCode",required = false) String attendanceClassCode) {
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

        AttendanceClass attendanceClass = AttendanceClass.builder()
                .companyId(companyId)
                .attendanceClassCode(attendanceClassCode)
                .build();

        return attendanceClassService.find(attendanceClass);
    }

    @GetMapping("/admin/company/attendance-class")
    public List<AttendanceClass> find (@RequestBody AttendanceClassRequest attendanceClassRequest) {
        //return attendanceClassService.find(attendanceClassRequest);
        return null;
    }

    @PostMapping("/classes")
    public List<AttendanceClass> save (HttpServletRequest request, HttpServletResponse response, @RequestBody AttendanceClassRequest attendanceClassRequest) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        //  アクセスしてきたユーザーがuriに含まれるcompanyIdに所属しているかチェック
        User loginUser = User.builder()
                .companyId(attendanceClassRequest.getCompanyId())
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return new ArrayList<>();
        }
        List<AttendanceClass> attendanceClasses = attendanceClassRequest.getAttendanceClasses()
                .stream()
                .filter(attendanceClass -> attendanceClass.getCompanyId() != null)
                .filter(attendanceClass -> attendanceClass.getAttendanceClassCode() != null)
                .filter(attendanceClass -> attendanceClass.getAttendanceClassName() != null)
                .collect(Collectors.toList());

        if (attendanceClasses.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new ArrayList<>();
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

    @DeleteMapping({"/companies/{companyId}/classes","/companies/{companyId}/classes/{attendanceClassCode}"})
    public String delete (HttpServletRequest request,HttpServletResponse response,
                          @PathVariable(value = "companyId") Integer companyId,
                          @PathVariable(value = "attendanceClassCode",required = false) String attendanceClassCode) {

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

        AttendanceClass attendanceClass = AttendanceClass.builder()
                .companyId(companyId)
                .attendanceClassCode(attendanceClassCode)
                .build();
        List<AttendanceClass> attendanceClasses = attendanceClassService.find(attendanceClass);

        int deletedCount = 0;
        for (AttendanceClass a : attendanceClasses) {
            AttendanceClass ac = AttendanceClass.builder().companyId(a.getCompanyId()).attendanceClassCode(a.getAttendanceClassCode()).build();
            String deleteRet = attendanceClassService.delete(ac);
            deletedCount ++;
            if (deleteRet.length() > 0) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return deletedCount + "件目エラー";
            }
        }
        return deletedCount +"件削除";
    }

    @DeleteMapping("/admin/company/attendance-class")
    public String delete (HttpServletResponse response, @RequestBody AttendanceClassRequest attendanceClassRequest) {

        //List<AttendanceClass> attendanceClasses = attendanceClassService.find(attendanceClassRequest);
        //attendanceClassService.delete(attendanceClasses);
        return "";
    }


}
