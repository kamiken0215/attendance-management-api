package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    //  自分自身のユーザー情報の照会
    @GetMapping("/user")
    public List<User> find (HttpServletRequest request,HttpServletResponse response,@RequestBody UserRequest userRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if(userService.isNotExistUser(userRequest.getUserId(), loginUser)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return userService.find(userRequest);
    }

    //  自社の社員の照会
    @GetMapping("/company/user")
    public List<User> findCompanyUser (HttpServletRequest request,HttpServletResponse response,@RequestBody UserRequest userRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if(userService.isNotExistCompany(loginUser, userRequest.getCompanyId())) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return userService.find(userRequest);
    }


    @GetMapping("/admin/user")
    public List<User> findAll (@RequestBody UserRequest userRequest) {
        return userService.find(userRequest);
    }

    //  自分自身のユーザー情報の編集
    @PostMapping("/user")
    public User save (HttpServletRequest request,HttpServletResponse response,@RequestBody UserRequest userRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if(userService.isNotExistUser(userRequest.getUserId(), loginUser)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        User user = new User();
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPaidHolidays(userRequest.getPaidHolidays());
        user.setIsActive(userRequest.getIsActive());
        user.setCompanyId(userRequest.getCompanyId());
        user.setDepartmentCode(userRequest.getDepartmentCode());
        user.setRoleCode(userRequest.getRoleCode());
        return userService.save(user);
    }


    //  自社の社員の登録・編集
    @PostMapping("/company/user")
    public User saveCompanyUser (HttpServletRequest request,HttpServletResponse response,@RequestBody UserRequest userRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if(userService.isNotExistCompany(loginUser, userRequest.getCompanyId())) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        User user = new User();
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPaidHolidays(userRequest.getPaidHolidays());
        user.setIsActive(userRequest.getIsActive());
        user.setCompanyId(userRequest.getCompanyId());
        user.setDepartmentCode(userRequest.getDepartmentCode());
        user.setRoleCode(userRequest.getRoleCode());
        return userService.save(user);
    }

    @PostMapping("/admin/user")
    public User saveAll (@RequestBody UserRequest userRequest) {
        User user = new User();
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPaidHolidays(userRequest.getPaidHolidays());
        user.setIsActive(userRequest.getIsActive());
        user.setCompanyId(userRequest.getCompanyId());
        user.setDepartmentCode(userRequest.getDepartmentCode());
        user.setRoleCode(userRequest.getRoleCode());
        return userService.save(user);
    }

    //  自社の社員の登録・編集
    @DeleteMapping("/company/user")
    public void deleteUser (HttpServletRequest request,HttpServletResponse response,@RequestBody UserRequest userRequest) {
        String token = request.getHeader("Authorization").substring(7);
        String loginUser = jwtProvider.getLoginFromToken(token);
        if(userService.isNotExistUser(userRequest.getUserId(), loginUser)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        User user = new User();
        user.setCompanyId(userRequest.getCompanyId());
        user.setDepartmentCode(userRequest.getDepartmentCode());
        user.setUserId(userRequest.getUserId());
        userService.delete(user);
    }

    @DeleteMapping("/admin/user")
    public void deleteUser (@RequestBody UserRequest userRequest) {
        User user = new User();
        user.setCompanyId(userRequest.getCompanyId());
        user.setDepartmentCode(userRequest.getDepartmentCode());
        user.setUserId(userRequest.getUserId());
        userService.delete(user);
    }

}
