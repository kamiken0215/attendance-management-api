package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.Roles;
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
    public DepartmentResponse find (HttpServletRequest request,HttpServletResponse response,
                                  @PathVariable(value = "companyId") Integer companyId,
                                  @PathVariable(value = "departmentCode",required = false) String departmentCode)  {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        if (!(email.length() > 0)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return DepartmentResponse.builder()
                    .error("不正なトークンです")
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
            return DepartmentResponse.builder()
                    .error("不正なユーザー")
                    .build();
        }

        if (!(Integer.parseInt(authUser.getRoleCode().replaceFirst("^0+", "")) >= Roles.COMPANY_READ_WRITE)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return DepartmentResponse.builder()
                    .error("権限がありません")
                    .build();
        }

        Department department = Department.builder()
                .companyId(companyId)
                .departmentCode(departmentCode)
                .build();

        List<Department> departments = departmentService.find(department);

        if (departments.size() == 0) {
            return DepartmentResponse.builder()
                    .error("データがありません")
                    .build();
        }

        return DepartmentResponse.builder()
                .departments(departments)
                .build();
    }

    @PostMapping("/departments")
    public CrudResponse save (HttpServletRequest request, HttpServletResponse response, @RequestBody DepartmentRequest departmentRequest) {
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
                .companyId(departmentRequest.getCompanyId())
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

        if (!(Integer.parseInt(authUser.getRoleCode().replaceFirst("^0+", "")) >= Roles.COMPANY_READ_WRITE_SETTING)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .number(0)
                    .message("権限がありません")
                    .ok(false)
                    .build();
        }

        List<Department> departments = departmentRequest.getDepartments()
                .stream()
                .filter(department -> department.getCompanyId() != null)
                .filter(department -> department.getDepartmentCode() != null)
                .filter(department -> department.getDepartmentName() != null)
                .collect(Collectors.toList());

        if (departments.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return CrudResponse.builder()
                    .number(0)
                    .message("追加可能なデータはありません")
                    .ok(false)
                    .build();
        }

        int savedCount = 0;
        for (Department d : departments) {
            Object result = departmentService.save(d);
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

    @DeleteMapping({"/companies/{companyId}/departments","/companies/{companyId}/departments/{departmentCode}"})
    public CrudResponse delete (HttpServletRequest request,HttpServletResponse response,
                          @PathVariable(value = "companyId") Integer companyId,
                          @PathVariable(value = "departmentCode",required = false) String departmentCode) {
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

        if (!(Integer.parseInt(authUser.getRoleCode().replaceFirst("^0+", "")) >= Roles.COMPANY_READ_WRITE_SETTING)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .number(0)
                    .message("権限がありません")
                    .ok(false)
                    .build();
        }

        Department department = Department.builder()
                .companyId(companyId)
                .departmentCode(departmentCode)
                .build();

        List<Department> departments = departmentService.find(department);

        if (departments.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return CrudResponse.builder()
                    .number(0)
                    .message("削除可能なデータはありません")
                    .ok(false)
                    .build();
        }

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
                        .build();
            }
        }
        return CrudResponse.builder()
                .number(deletedCount)
                .message(deletedCount + "件削除")
                .ok(true)
                .build();
    }
}
