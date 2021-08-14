package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.entitiy.UserEntity;
import com.kentarokamiyama.attendancemanagementapi.service.AuthService;
import com.kentarokamiyama.attendancemanagementapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register")
    public String registerUser(@RequestBody RegistrationRequest registrationRequest) {
        User u = new User();
        u.setUserName(registrationRequest.getUserName());
        u.setCompanyId(registrationRequest.getCompanyId());
        u.setDepartmentCode(registrationRequest.getDepartmentCode());
        u.setRoleCode(registrationRequest.getRoleCode());
        u.setEmail(registrationRequest.getEmail());
        u.setPassword(registrationRequest.getPassword());
        u.setPaidHolidays(registrationRequest.getPaidHolidays());
        u.setIsActive(registrationRequest.getIsActive());
        authService.saveUser(u);
        return "OK";
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest authRequest) {
        User user = authService.findByEmailAndPassword(authRequest.getEmail(),authRequest.getPassword());
        String token = jwtProvider.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

}
