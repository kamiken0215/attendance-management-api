package com.kentarokamiyama.attendancemanagementapi.config;

import com.kentarokamiyama.attendancemanagementapi.config.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/companies/*").hasAnyRole("ADMIN","COMPANY","USER","ATTENDANCE")
                .antMatchers("/departments/*").hasAnyRole("ADMIN","COMPANY","USER","ATTENDANCE")
                .antMatchers("/users/*").hasAnyRole("ADMIN","COMPANY","USER","ATTENDANCE")
                .antMatchers("/attendances/*").hasAnyRole("ADMIN","COMPANY","USER","ATTENDANCE")
                .antMatchers("/attendances").hasAnyRole("ADMIN","COMPANY","USER","ATTENDANCE")
//                .antMatchers("/company/*").hasAnyRole("ADMIN","COMPANY")
//                .antMatchers("/companies/**").hasAnyRole("ADMIN","COMPANY")
//                .antMatchers("/companies/*/departments/*/users/*").hasAnyRole("ADMIN","COMPANY","USER")
//                .antMatchers("/attendance/*").hasAnyRole("ADMIN","COMPANY","USER","ATTENDANCE")
                .antMatchers("/register","/auth").permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
