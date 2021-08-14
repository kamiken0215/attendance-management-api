package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.repository.CompanyRepository;
import com.kentarokamiyama.attendancemanagementapi.repository.DepartmentRepository;
import com.kentarokamiyama.attendancemanagementapi.repository.RoleRepository;
import com.kentarokamiyama.attendancemanagementapi.repository.UserRepository;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Company;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Department;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Role;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User saveUser(User user) {
        Company company = companyRepository.findByCompanyId(user.getCompanyId());
        Department department = departmentRepository.findByCompanyIdDepartmentCode(user.getCompanyId(),user.getDepartmentCode());
        Role role = roleRepository.findByRoleCode(user.getRoleCode());
        user.setCompany(company);
        user.setDepartment(department);
        user.setRole(role);
        user.setRoleCode(user.getRoleCode());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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
