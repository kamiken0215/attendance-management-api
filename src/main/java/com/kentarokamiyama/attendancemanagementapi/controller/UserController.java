package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Department;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.model.CrudResponse;
import com.kentarokamiyama.attendancemanagementapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @GetMapping({"companies/{companyId}/users",
            "companies/{companyId}/departments/{departmentCode}/users",
            "companies/{companyId}/departments/{departmentCode}/users/{userId}"})
    public List<UserResponse> find (HttpServletRequest request,HttpServletResponse response,
                              @PathVariable(value = "companyId") Integer companyId,
                              @PathVariable(value = "departmentCode",required = false) String departmentCode,
                              @PathVariable(value = "userId",required = false) Integer userId) {

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

        //  本人確認
        if (userId != null) {
            if (!userId.equals(loginUser.getUserId())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
        }

        //  uriに含まれる情報だけ
        User user = User.builder()
                .companyId(companyId)
                .departmentCode(departmentCode)
                .userId(userId)
                .build();

        List<User> users = userService.find(user);
        List<UserResponse> userResponses = new ArrayList<>();
        for (User u:users) {
            UserResponse userResponse = UserResponse.builder()
                    .userId(u.getUserId())
                    .userName(u.getUserName())
                    .companyId(u.getCompanyId())
                    .companyName(u.getCompany().getCompanyName())
                    .departmentCode(u.getDepartmentCode())
                    .departmentName(u.getDepartment().getDepartmentName())
                    .email(u.getEmail())
                    .isActive(u.getIsActive())
                    .paidHolidays(u.getPaidHolidays())
                    .roleCode(u.getRoleCode())
                    .build();
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    @PostMapping("/users")
    public UserResponse save (HttpServletRequest request,HttpServletResponse response,@RequestBody UserRequest userRequest) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        User loginUser = User.builder()
                .userId(userRequest.getUserId())
                .companyId(userRequest.getCompanyId())
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        //  アクセスしてきたユーザーがuriに含まれるcompanyIdに所属しているかチェック
        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        //  本人確認
        if (userRequest.getUserId() != null) {
            if (!userRequest.getUserId().equals(authUser.getUserId())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
        }

        User user = User.builder()
                .userId(userRequest.getUserId())
                .userName(userRequest.getUserName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .paidHolidays(userRequest.getPaidHolidays())
                .isActive(userRequest.getIsActive())
                .companyId(userRequest.getCompanyId())
                .departmentCode(userRequest.getDepartmentCode())
                .roleCode(userRequest.getRoleCode())
                .build();

        Object result = userService.save(user);

        if (result instanceof User) {
            User u = (User) result;
            return UserResponse.builder()
                    .userId(u.getUserId())
                    .userName(u.getUserName())
                    .companyId(u.getCompanyId())
                    .departmentCode(u.getDepartmentCode())
                    .email(u.getEmail())
                    .isActive(u.getIsActive())
                    .paidHolidays(u.getPaidHolidays())
                    .roleCode(u.getRoleCode())
                    .build();
        } else {
            return UserResponse.builder().error(result.toString()).build();
        }
    }

    @PostMapping("companies/{companyId}/users")
    public List<UserResponse> saveAll (HttpServletRequest request,HttpServletResponse response,@RequestBody List<UserRequest> userRequests,
                               @PathVariable(value = "companyId") Integer companyId) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        User loginUser = User.builder()
                .companyId(companyId)
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        //  アクセスしてきたユーザーがuriに含まれるcompanyIdに所属しているかチェック
        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        List<UserResponse> resultUsers = new ArrayList<>();

        for (UserRequest u : userRequests) {
            User user = User.builder()
                    .userName(u.getUserName())
                    .email(u.getEmail())
                    .password(passwordEncoder.encode(u.getPassword()))
                    .paidHolidays(u.getPaidHolidays())
                    .isActive(u.getIsActive())
                    .companyId(u.getCompanyId())
                    .departmentCode(u.getDepartmentCode())
                    .roleCode(u.getRoleCode())
                    .build();

            Object result = userService.save(user);

            if (result instanceof User) {
                User r = (User) result;
                UserResponse userResponse = UserResponse.builder()
                        .userId(r.getUserId())
                        .userName(r.getUserName())
                        .companyId(r.getCompanyId())
                        .departmentCode(r.getDepartmentCode())
                        .email(r.getEmail())
                        .isActive(r.getIsActive())
                        .paidHolidays(r.getPaidHolidays())
                        .roleCode(r.getRoleCode())
                        .build();
                resultUsers.add(userResponse);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                List<UserResponse> l = new ArrayList<>();
                UserResponse userResponse = UserResponse.builder().error(result.toString()).build();
                l.add(userResponse);
                return l;
            }
        }

        return resultUsers;
    }

    @PostMapping("/admin/users")
    public UserResponse saveAll (@RequestBody UserRequest userRequest) {

        User user = new User();
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPaidHolidays(userRequest.getPaidHolidays());
        user.setIsActive(userRequest.getIsActive());
        user.setCompanyId(userRequest.getCompanyId());
        user.setDepartmentCode(userRequest.getDepartmentCode());
        user.setRoleCode(userRequest.getRoleCode());

        Object result = userService.save(user);

        if (result instanceof User) {
            User u = (User) result;
            return UserResponse.builder()
                    .userId(u.getUserId())
                    .userName(u.getUserName())
                    .companyId(u.getCompanyId())
                    .departmentCode(u.getDepartmentCode())
                    .email(u.getEmail())
                    .isActive(u.getIsActive())
                    .paidHolidays(u.getPaidHolidays())
                    .roleCode(u.getRoleCode())
                    .build();
        } else {
            return UserResponse.builder().error(result.toString()).build();
        }
    }

    //  自社の社員の削除
    @DeleteMapping({"companies/{companyId}/users",
            "companies/{companyId}/departments/{departmentCode}/users",
            "companies/{companyId}/departments/{departmentCode}/users/{userId}"})
    public String deleteUser (HttpServletRequest request,HttpServletResponse response,
                            @PathVariable(value = "companyId") Integer companyId,
                            @PathVariable(value = "departmentCode",required = false) String departmentCode,
                            @PathVariable(value = "userId",required = false) Integer userId) {

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
            return CrudResponse.builder()
                    .message("不正なユーザー")
                    .ok(false)
                    .build()
                    .toJson();
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
                    .message("該当ユーザーなし")
                    .ok(false)
                    .build()
                    .toJson();
        }

        int deletedCount = 0;
        for (User u : users) {
            String result = userService.delete(u);
            deletedCount ++;
            if (result.length() > 0) {
                return deletedCount + "件目エラー";
            }
        }

        return deletedCount +"件削除";
    }

    @DeleteMapping("/admin/users")
    public void deleteUser (@RequestBody UserRequest userRequest) {
        User user = new User();
        user.setCompanyId(userRequest.getCompanyId());
        user.setDepartmentCode(userRequest.getDepartmentCode());
        user.setUserId(userRequest.getUserId());
        String result = userService.delete(user);
    }

}
