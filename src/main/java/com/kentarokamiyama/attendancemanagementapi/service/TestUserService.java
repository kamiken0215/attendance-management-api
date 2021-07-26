package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.Repository.CompanyRepository;
import com.kentarokamiyama.attendancemanagementapi.Repository.DepartmentRepository;
import com.kentarokamiyama.attendancemanagementapi.Repository.RoleRepository;
import com.kentarokamiyama.attendancemanagementapi.Repository.UserRepository;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Company;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Department;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Role;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TestUserService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        Company company = companyRepository.findById(user.getCompanyId());
        Department department = departmentRepository.findByCode(user.getDepartmentCode());
        Role role = roleRepository.findByCode(user.getRoleCode());
        user.setCompany(company);
        user.setDepartment(department);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByEmailAndPassword(String email,String password) {
        User user = findByEmail(email);
        if (user != null) {
            if (passwordEncoder.matches(password,user.getPassword())) {
                return user;
            }
        }
        return null;
    }

}
