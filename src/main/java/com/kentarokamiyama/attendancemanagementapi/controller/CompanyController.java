package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.*;
import com.kentarokamiyama.attendancemanagementapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

    @GetMapping("/companies/{companyId}")
    public CompanyResponse find (HttpServletRequest request, HttpServletResponse response,
                                       @PathVariable(value = "companyId") Integer companyId) {
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
            return CompanyResponse.builder()
                    .companyId(null)
                    .companyName("")
                    .departments(new HashSet<>())
                    .error("不正なユーザーです")
                    .build();
        }

        Company result = companyService.findOne(companyId);

        if (result == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return CompanyResponse.builder()
                    .companyId(null)
                    .companyName("")
                    .departments(new HashSet<>())
                    .error("みつかりませんでした")
                    .build();
        } else {
            return CompanyResponse.builder()
                    .companyId(result.getCompanyId())
                    .companyName(result.getCompanyName())
                    .departments(result.getDepartments().stream().sorted(Comparator.comparing(Department::getDepartmentCode)).collect(Collectors.toCollection(LinkedHashSet::new)))
                    .build();
        }
    }

    @GetMapping("admin/company")
    public List<Company> find (@RequestBody CompanyRequest companyRequest) {
        return companyService.find(companyRequest);
    }

    @PostMapping("/companies")
    public CompanyResponse saveCompany (HttpServletRequest request, HttpServletResponse response, @RequestBody CompanyRequest companyRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        //  アクセスしてきたユーザーがuriに含まれるcompanyIdに所属しているかチェック
        User loginUser = User.builder()
                .companyId(companyRequest.getCompanyId())
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        Company company = Company.builder()
                .companyId(companyRequest.getCompanyId())
                .companyName(companyRequest.getCompanyName())
                .build();

        Object result = companyService.save(company);
        if (result instanceof Company) {
            Company c = (Company) result;
            return CompanyResponse.builder()
                    .companyId(c.getCompanyId())
                    .companyName(c.getCompanyName())
                    .build();
        } else {
            return CompanyResponse.builder().error(result.toString()).build();
        }
    }

    @PostMapping("admin/company")
    public CompanyResponse saveCompany (@RequestBody CompanyRequest companyRequest) {
        Company company = new Company();
        company.setCompanyName(companyRequest.getCompanyName());
        Object result = companyService.save(company);
        if (result instanceof Company) {
            Company c = (Company) result;
            return CompanyResponse.builder()
                    .companyId(c.getCompanyId())
                    .companyName(c.getCompanyName())
                    //.departments(c.getDepartment())
                    .build();
        } else {
            return CompanyResponse.builder().error(result.toString()).build();
        }
    }

    @DeleteMapping("/companies/{companyId}")
    public String deleteCompany (HttpServletRequest request,HttpServletResponse response,
                                 @PathVariable(value = "companyId") Integer companyId) {
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

        String checkResult = beforeCompanyDelete(companyId,email);

        if (checkResult.length() != 0) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return checkResult;
        }

        Company company = Company.builder().companyId(companyId).build();
        companyService.delete(company);

        return "";
    }

    //  1.count attendance
    //  2.count attendance_code
    //  3.count department
    //  4.count user (except admin user)
    //  4.1~4 each counts 0 -> delete company
    private String beforeCompanyDelete (Integer companyId,String email) {
//        UserRequest userRequest = new UserRequest();
//        userRequest.setCompanyId(companyId);
        User user = User.builder()
                .companyId(companyId)
                .build();
        List<User> users = userService.find(user);

        //  1.
        for (User u : users) {
            Attendance attendance = new Attendance();
            attendance.setUserId(u.getUserId());
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
                .filter(u -> !u.getEmail().equals(email))
                .map(User::getUserId)
                .collect(Collectors.toList());

        if (userService.count(exceptRootUserIds) != 0) {
            return "ルートユーザー以外のユーザーが削除されていません";
        }

        return "";

    }

}
