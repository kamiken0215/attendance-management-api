package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.entitiy.UserEntity;
import com.kentarokamiyama.attendancemanagementapi.model.CrudResponse;
import com.kentarokamiyama.attendancemanagementapi.model.UserResponseModel;
import com.kentarokamiyama.attendancemanagementapi.service.AuthService;
import com.kentarokamiyama.attendancemanagementapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public CrudResponse registerUser(@RequestBody UserRequest userRequest) {

        User u = new User();

        if (userRequest.getUsers().size() != 1) {
            return CrudResponse.builder()
                    .number(1)
                    .message("データが不正です")
                    .ok(false)
                    .build();
        }

        u = userRequest.getUsers().get(0);

//        u.setUserName(registrationRequest.getUserName());
//        u.setCompanyId(registrationRequest.getCompanyId());
//        u.setDepartmentCode(registrationRequest.getDepartmentCode());
//        u.setRoleCode(registrationRequest.getRoleCode());
//        u.setEmail(registrationRequest.getEmail());
//        u.setPassword(registrationRequest.getPassword());
//        u.setPaidHolidays(registrationRequest.getPaidHolidays());
//        u.setIsActive(registrationRequest.getIsActive());
        u.setPassword(passwordEncoder.encode(u.getPassword()));

        //Object result = authService.saveUser(u);
        Object result = userService.save(u);

        if (result instanceof String) {

            return CrudResponse.builder()
                    .number(1)
                    .message(result.toString())
                    .ok(false)
                    .build();

        } else {
            return CrudResponse.builder()
                    .number(1)
                    .message("ok")
                    .ok(true)
                    .build();
        }
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest authRequest) {
        User user = authService.findByEmailAndPassword(authRequest.getEmail(),authRequest.getPassword());
        String token = jwtProvider.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    @PostMapping("/auth/token")
    public UserResponse auth(HttpServletRequest request,HttpServletResponse response) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        if (!(email.length() > 0)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return UserResponse.builder().error("不正なトークンです、再ログインしてください").build();
        } else {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            User loginUser = User.builder()
                    .email(email)
                    .build();

            User authUser = userService.findOne(loginUser);

            if(authUser == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return UserResponse.builder().error("不正なユーザーです").build();
            } else {
                List<UserResponseModel> userResponseModels = new ArrayList<>();
                assert authUser.getCompany() != null;
                assert authUser.getDepartment() != null;
                UserResponseModel model = UserResponseModel.builder()
                        .userId(authUser.getUserId())
                        .userName(authUser.getUserName())
                        .companyId(authUser.getCompanyId())
                        .companyName(authUser.getCompany().getCompanyName())
                        .departmentCode(authUser.getDepartmentCode())
                        .departmentName(authUser.getDepartment().getDepartmentName())
                        .email(authUser.getEmail())
                        .isActive(authUser.getIsActive())
                        .paidHolidays(authUser.getPaidHolidays())
                        .roleCode(authUser.getRoleCode())
                        .build();
                userResponseModels.add(model);
                return UserResponse.builder().users(userResponseModels).build();
            }
        }
    }

}
