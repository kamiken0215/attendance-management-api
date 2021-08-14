package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.*;
import com.kentarokamiyama.attendancemanagementapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CompanyController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private AttendanceClassService attendanceClassService;

    @GetMapping("/company")
    public List<Company> find (HttpServletRequest request, HttpServletResponse response,@RequestBody CompanyRequest companyRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if (companyService.isNotExistCompany(loginUser, companyRequest.getCompanyId())) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return null;
        }
        return companyService.find(companyRequest);
    }

    @GetMapping("admin/company")
    public List<Company> find (@RequestBody CompanyRequest companyRequest) {
        return companyService.find(companyRequest);
    }

    @PostMapping("/company")
    public Company saveCompany (HttpServletRequest request, HttpServletResponse response,@RequestBody CompanyRequest companyRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if (companyService.isNotExistCompany(loginUser, companyRequest.getCompanyId())) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return null;
        }
        Company company = new Company();
        company.setCompanyName(companyRequest.getCompanyName());
        return companyService.save(company);
    }

    @PostMapping("admin/company")
    public Company saveCompany (@RequestBody CompanyRequest companyRequest) {
        Company company = new Company();
        company.setCompanyName(companyRequest.getCompanyName());
        return companyService.save(company);
    }

    @DeleteMapping("/company")
    public String deleteCompany (HttpServletRequest request,HttpServletResponse response, @RequestBody CompanyRequest companyRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if (companyService.isNotExistCompany(loginUser, companyRequest.getCompanyId())) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return "ユーザーが見つかりません";
        }

        String checkResult = beforeCompanyDelete(companyRequest.getCompanyId(),loginUser);

        if (checkResult.length() != 0) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return checkResult;
        }

        Company company = new Company();
        company.setCompanyId(companyRequest.getCompanyId());
        companyService.delete(company);

        return "";
    }

    //  1.count attendance
    //  2.count attendance_code
    //  3.count department
    //  4.count user (except admin user)
    //  4.1~4 each counts 0 -> delete company
    private String beforeCompanyDelete (Integer companyId,String loginUser) {
        UserRequest userRequest = new UserRequest();
        userRequest.setCompanyId(companyId);
        List<User> users = userService.find(userRequest);

        //  1.
        for (User user : users) {
            Attendance attendance = new Attendance();
            attendance.setUserId(user.getUserId());
            if (attendanceService.count(attendance) != 0) {
                return "出勤データが削除されていません";
            }
        }

        //  2.
        AttendanceClass attendanceClass = new AttendanceClass();
        attendanceClass.setCompanyId(companyId);
        if (attendanceClassService.count(attendanceClass) != 0) {
            return "出勤区分データが削除されていません";
        }

        //  3.
        Department department = new Department();
        department.setCompanyId(companyId);
        if (departmentService.count(department) != 0) {
            return "部門データが削除されていません";
        }

        //  4.
        final List<Integer> exceptRootUserIds = users.stream()
                .filter(user -> !user.getEmail().equals(loginUser))
                .map(User::getUserId)
                .collect(Collectors.toList());

        if (userService.count(exceptRootUserIds) != 0) {
            return "ルートユーザー以外のユーザーが削除されていません";
        }

        return "";

    }

}