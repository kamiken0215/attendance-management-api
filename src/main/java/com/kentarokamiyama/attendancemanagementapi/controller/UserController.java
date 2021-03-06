package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.Roles;
import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.controller.request.UserRequest;
import com.kentarokamiyama.attendancemanagementapi.controller.response.UserResponse;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.model.CrudResponse;
import com.kentarokamiyama.attendancemanagementapi.model.UserResponseModel;
import com.kentarokamiyama.attendancemanagementapi.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Log
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
    public UserResponse find (HttpServletRequest request, HttpServletResponse response,
                              @PathVariable(value = "companyId") Integer companyId,
                              @PathVariable(value = "departmentCode",required = false) String departmentCode,
                              @PathVariable(value = "userId",required = false) Integer userId) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        if (!(email.length() > 0)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return UserResponse.builder().error("???????????????????????????").build();
        }

        //  ???????????????????????????????????????uri???????????????companyId????????????????????????????????????
        User loginUser = User.builder()
                .companyId(companyId)
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return UserResponse.builder().error("???????????????????????????").build();
        }

        if (!(Integer.parseInt(authUser.getRoleCode().replaceFirst("^0+", "")) >= Roles.ATTENDANCE_ONLY_READ)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return UserResponse.builder()
                    .error("????????????????????????")
                    .build();
        }

        //  ????????????
        if (userId != null) {
            if (!userId.equals(loginUser.getUserId())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return UserResponse.builder()
                        .error("???????????????????????????")
                        .build();
            }
        }

        //  uri???????????????????????????
        User user = User.builder()
                .companyId(companyId)
                .departmentCode(departmentCode)
                .userId(userId)
                .build();

        List<User> users = userService.find(user);
        LinkedList<UserResponseModel> userResponseModels = new LinkedList<>();
        for (User u:users) {
            assert u.getCompany() != null;
            assert u.getDepartment() != null;
            assert u.getRole() != null;
            UserResponseModel model = UserResponseModel.builder()
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
                    .roleName((u.getRole().getRoleName()))
                    .build();
            userResponseModels.add(model);
        }

        return UserResponse.builder()
                .users(userResponseModels)
                .build();
    }

    @PostMapping("/users")
    public CrudResponse save (HttpServletRequest request,HttpServletResponse response,@RequestBody UserRequest userRequest) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        if (!(email.length() > 0)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .number(0)
                    .message("???????????????????????????")
                    .ok(false)
                    .build();
        }

        User loginUser = User.builder()
                .companyId(userRequest.getCompanyId())
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        //  ???????????????????????????????????????uri???????????????companyId????????????????????????????????????
        if(authUser == null) {
            log.severe("???????????????");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .number(0)
                    .message("???????????????????????????")
                    .ok(false)
                    .build();
        }

        if (!(Integer.parseInt(authUser.getRoleCode().replaceFirst("^0+", "")) >= Roles.USER_READ_WRITE)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .number(0)
                    .message("????????????????????????")
                    .ok(false)
                    .build();
        }

        List<User> users = userRequest.getUsers()
                .stream()
                .filter(user -> user.getCompanyId() != null && Objects.equals(user.getCompanyId(), userRequest.getCompanyId()))
                .filter(user -> user.getDepartmentCode() != null)
                .filter(user -> user.getUserName() != null)
                .filter(user -> user.getEmail() != null && user.getEmail().length() > 0)
//                .filter(user -> user.getPassword() != null)
                .filter(user -> user.getPaidHolidays() != null)
                .filter(user -> user.getIsActive() != null)
                .filter(user -> user.getRoleCode() != null)
                .collect(Collectors.toList());

        if (users.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return CrudResponse.builder()
                    .number(0)
                    .message("??????????????????????????????????????????")
                    .ok(false)
                    .build();
        }

        int savedCount = 0;
        for (User u : users) {
            try {
                Active enm = Active.valueOf(u.getIsActive());
            } catch (IllegalArgumentException e) {
                log.severe(e.toString());
                return CrudResponse.builder()
                        .number(0)
                        .message("???????????????"+u.getUserName()+"???iaActive???on???off?????????????????????")
                        .ok(false)
                        .build();
            }
            User existUser = userService.findById(u.getUserId()).orElse(null);

            Object result;
            if (existUser != null) {
                existUser.setCompanyId(u.getCompanyId());
                existUser.setUserId(u.getUserId());
                existUser.setUserName(u.getUserName());
                existUser.setEmail(u.getEmail());
                existUser.setPaidHolidays(u.getPaidHolidays());
                existUser.setIsActive(u.getIsActive());
                existUser.setDepartmentCode(u.getDepartmentCode());
                existUser.setRoleCode(u.getRoleCode());
                if (u.getPassword() != null) {
                    existUser.setPassword(passwordEncoder.encode(u.getPassword()));
                }
                result = userService.save(existUser);
            } else {
                u.setPassword(passwordEncoder.encode(u.getPassword()));
                result = userService.save(u);
            }
            if (result instanceof String) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return CrudResponse.builder()
                        .number(savedCount)
                        .message(result.toString())
                        .ok(false)
                        .build();
            }
            savedCount++;
        }

        return CrudResponse.builder()
                .number(savedCount)
                .message(savedCount + "???")
                .ok(true)
                .build();
    }

//    @PostMapping("/new/users")
//    public CrudResponse create (HttpServletRequest request,HttpServletResponse response,@RequestBody UserRequest userRequest) {
//
//        String token = request.getHeader("Authorization").substring(7);
//        String email = jwtProvider.getLoginFromToken(token);
//
//        if (!(email.length() > 0)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return CrudResponse.builder()
//                    .number(0)
//                    .message("???????????????????????????")
//                    .ok(false)
//                    .build();
//        }
//
//        User loginUser = User.builder()
//                .companyId(userRequest.getCompanyId())
//                .email(email)
//                .build();
//
//        User authUser = userService.findOne(loginUser);
//
//        //  ???????????????????????????????????????uri???????????????companyId????????????????????????????????????
//        if(authUser == null) {
//            log.severe("???????????????");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return CrudResponse.builder()
//                    .number(0)
//                    .message("???????????????????????????")
//                    .ok(false)
//                    .build();
//        }
//
//        if (!(Integer.parseInt(authUser.getRoleCode().replaceFirst("^0+", "")) >= Roles.USER_READ_WRITE)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return CrudResponse.builder()
//                    .number(0)
//                    .message("????????????????????????")
//                    .ok(false)
//                    .build();
//        }
//
//        List<User> users = userRequest.getUsers()
//                .stream()
//                .filter(user -> user.getCompanyId() != null && Objects.equals(user.getCompanyId(), userRequest.getCompanyId()))
//                .filter(user -> user.getDepartmentCode() != null)
//                .filter(user -> user.getUserName() != null)
//                .filter(user -> user.getEmail() != null && user.getEmail().length() > 0)
//                .filter(user -> user.getPassword() != null)
//                .filter(user -> user.getPaidHolidays() != null)
//                .filter(user -> user.getIsActive() != null)
//                .filter(user -> user.getRoleCode() != null)
//                .collect(Collectors.toList());
//
//        if (users.size() == 0) {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            return CrudResponse.builder()
//                    .number(0)
//                    .message("??????????????????????????????????????????")
//                    .ok(false)
//                    .build();
//        }
//
//        int savedCount = 0;
//        for (User u : users) {
//            try {
//                Active enm = Active.valueOf(u.getIsActive());
//            } catch (IllegalArgumentException e) {
//                log.severe(e.toString());
//                return CrudResponse.builder()
//                        .number(0)
//                        .message("???????????????"+u.getUserName()+"???iaActive???on???off?????????????????????")
//                        .ok(false)
//                        .build();
//            }
//
//            Object result = userService.save(u);
//
//            if (result instanceof String) {
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                return CrudResponse.builder()
//                        .number(savedCount)
//                        .message(result.toString())
//                        .ok(false)
//                        .build();
//            }
//            savedCount++;
//        }
//
//        return CrudResponse.builder()
//                .number(savedCount)
//                .message(savedCount + "???")
//                .ok(true)
//                .build();
//    }

    //  ????????????????????????
    @DeleteMapping({"companies/{companyId}/users",
            "companies/{companyId}/departments/{departmentCode}/users",
            "companies/{companyId}/departments/{departmentCode}/users/{userId}"})
    public String deleteUser (HttpServletRequest request,HttpServletResponse response,
                            @PathVariable(value = "companyId") Integer companyId,
                            @PathVariable(value = "departmentCode",required = false) String departmentCode,
                            @PathVariable(value = "userId",required = false) Integer userId) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        if (!(email.length() > 0)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .message("???????????????????????????")
                    .ok(false)
                    .build()
                    .toJson();
        }

        //  ???????????????????????????????????????uri???????????????companyId????????????????????????????????????
        User loginUser = User.builder()
                .companyId(companyId)
                .email(email)
                .build();

        User authUser = userService.findOne(loginUser);

        if(authUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .message("?????????????????????")
                    .ok(false)
                    .build()
                    .toJson();
        }

        if (!(Integer.parseInt(authUser.getRoleCode().replaceFirst("^0+", "")) >= Roles.USER_READ_WRITE_SETTING)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CrudResponse.builder()
                    .message("?????????????????????")
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
                    .message("????????????????????????")
                    .ok(false)
                    .build()
                    .toJson();
        }

        int deletedCount = 0;
        for (User u : users) {
            String result = userService.delete(u);
            deletedCount ++;
            if (result.length() > 0) {
                return CrudResponse.builder()
                        .message(deletedCount + "???????????????")
                        .ok(false)
                        .build()
                        .toJson();
            }
        }

        return CrudResponse.builder()
                .message(deletedCount + "?????????")
                .ok(true)
                .build()
                .toJson();
    }

    protected enum Active {
        on,off
    }
}
