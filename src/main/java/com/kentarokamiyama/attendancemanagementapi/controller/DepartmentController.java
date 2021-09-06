package com.kentarokamiyama.attendancemanagementapi.controller;

import com.google.gson.Gson;
import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Department;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.model.CrudResponse;
import com.kentarokamiyama.attendancemanagementapi.service.DepartmentService;
import com.kentarokamiyama.attendancemanagementapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DepartmentController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;

    @GetMapping({"/companies/{companyId}/departments","/companies/{companyId}/departments/{departmentCode}"})
    public List<Department> find (HttpServletRequest request,HttpServletResponse response,
                                  @PathVariable(value = "companyId") Integer companyId,
                                  @PathVariable(value = "departmentCode",required = false) String departmentCode)  {
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
            return new ArrayList<>();
        }

        Department department = Department.builder()
                .companyId(companyId)
                .departmentCode(departmentCode)
                .build();

        return departmentService.find(department);
    }

    @GetMapping("/admin/company/department")
    public List<Department> find (@RequestBody DepartmentRequest departmentRequest) {
       // return departmentService.find(departmentRequest);
        return null;
    }

    @PostMapping("/departments")
    public List<Department> save (HttpServletRequest request, HttpServletResponse response, @RequestBody DepartmentRequest departmentRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        //  アクセスしてきたユーザーがuriに含まれるcompanyIdに所属しているかチェック
        User loginUser = User.builder()
                .companyId(departmentRequest.getCompanyId())
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return new ArrayList<>();
        }
        List<Department> departments = departmentRequest.getDepartments()
                .stream()
                .filter(department -> department.getCompanyId() != null)
                .filter(department -> department.getDepartmentCode() != null)
                .filter(department -> department.getDepartmentName() != null)
                .collect(Collectors.toList());

        if (departments.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new ArrayList<>();
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

    @DeleteMapping({"/companies/{companyId}/departments","/companies/{companyId}/departments/{departmentCode}"})
    public String delete (HttpServletRequest request,HttpServletResponse response,
                          @PathVariable(value = "companyId") Integer companyId,
                          @PathVariable(value = "departmentCode",required = false) String departmentCode) {
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

        Department department = Department.builder()
                .companyId(companyId)
                .departmentCode(departmentCode)
                .build();

        List<Department> departments = departmentService.find(department);

        int deletedCount = 0;
        for (Department d : departments) {
            String deleteRet = departmentService.delete(d);
            deletedCount ++;
            if (deleteRet.length() > 0) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return CrudResponse.builder()
                        .number(deletedCount)
                        .message(deleteRet)
                        .ok(false)
                        .build()
                        .toJson();
                //return deletedCount + "件目エラー";
            }
        }

        return deletedCount +"件削除";
    }

    @DeleteMapping("/admin/company/department")
    public String delete (HttpServletResponse response, @RequestBody DepartmentRequest departmentRequest) {
        //List<Department> departments = departmentService.find(departmentRequest);
        //departmentService.delete(departments);
        return "";
    }
}
