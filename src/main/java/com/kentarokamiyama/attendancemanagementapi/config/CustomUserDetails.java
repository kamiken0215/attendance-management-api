package com.kentarokamiyama.attendancemanagementapi.config;

import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.entitiy.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    //private String login;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

//    public static CustomUserDetails fromUserEntityToCustomUserDetails(UserEntity userEntity) {
//        CustomUserDetails c = new CustomUserDetails();
//        c.login = userEntity.getLogin();
//        c.password = userEntity.getPassword();
//        c.grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(userEntity.getRoleEntity().getName()));
//        return c;
//    }
    public static CustomUserDetails fromUserToCustomUserDetails(User user) {
        CustomUserDetails c = new CustomUserDetails();
        c.email = user.getEmail();
        c.password = user.getPassword();
        c.grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRoleName()));
        return c;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }


}
