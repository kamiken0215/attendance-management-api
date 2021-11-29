package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtProvider;
import com.kentarokamiyama.attendancemanagementapi.controller.response.RoleResponse;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Role;
import com.kentarokamiyama.attendancemanagementapi.service.RoleService;
import com.kentarokamiyama.attendancemanagementapi.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Log
public class RoleController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    public RoleResponse find (HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtProvider.getLoginFromToken(token);

        if (!(email.length() > 0)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return RoleResponse.builder().error("不正なトークンです").build();
        }


        List<Role> roles = roleService.find();

        if (roles.size() == 0) {
            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            return RoleResponse.builder().error("データが取得できませんでした").build();
        }

        return RoleResponse.builder().roles(roles).build();

    }
}
