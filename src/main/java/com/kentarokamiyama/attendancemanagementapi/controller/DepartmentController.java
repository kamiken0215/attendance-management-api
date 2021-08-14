package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Department;
import com.kentarokamiyama.attendancemanagementapi.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DepartmentController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/company/department")
    public List<Department> find (HttpServletRequest request, HttpServletResponse response, @RequestBody DepartmentRequest departmentRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if (departmentService.isNotCompanyUser(loginUser, departmentRequest.getCompanyId())) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return departmentService.find(departmentRequest);
    }

    @GetMapping("/admin/company/department")
    public List<Department> find (@RequestBody DepartmentRequest departmentRequest) {
        return departmentService.find(departmentRequest);
    }

    @PostMapping("/company/department")
    public List<Department> save (HttpServletRequest request, HttpServletResponse response, @RequestBody DepartmentRequest departmentRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if (departmentService.isNotCompanyUser(loginUser, departmentRequest.getCompanyId())) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        List<Department> departments = departmentRequest.getDepartments()
                .stream()
                .filter(department -> department.getCompanyId() != null)
                .filter(department -> department.getDepartmentCode() != null)
                .filter(department -> department.getDepartmentName() != null)
                .collect(Collectors.toList());

        if (departments.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return departmentService.save(departments);
    }

    @PostMapping("admin/company/department")
    public List<Department> save (HttpServletResponse response, @RequestBody DepartmentRequest departmentRequest) {

        List<Department> departments = departmentRequest.getDepartments()
                .stream()
                .filter(department -> department.getCompanyId() != null)
                .filter(department -> department.getDepartmentCode() != null)
                .filter(department -> department.getDepartmentName() != null)
                .collect(Collectors.toList());

        if (departments.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return departmentService.save(departments);
    }

    @DeleteMapping("/company/department")
    public String delete (HttpServletRequest request, HttpServletResponse response, @RequestBody DepartmentRequest departmentRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if (departmentService.isNotCompanyUser(loginUser, departmentRequest.getCompanyId())) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "ユーザーが見つかりません";
        }
        List<Department> departments = departmentService.find(departmentRequest);

        departmentService.delete(departments);
        return "";
    }

    @DeleteMapping("/admin/company/department")
    public String delete (HttpServletResponse response, @RequestBody DepartmentRequest departmentRequest) {
        List<Department> departments = departmentService.find(departmentRequest);
        departmentService.delete(departments);
        return "";
    }
}
