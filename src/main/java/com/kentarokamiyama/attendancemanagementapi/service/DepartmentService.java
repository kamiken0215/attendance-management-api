package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.controller.DepartmentRequest;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Department;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.repository.DepartmentRepository;
import com.kentarokamiyama.attendancemanagementapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Department> find (DepartmentRequest departmentRequest) {
        return departmentRepository.findAll(Specification
                .where(DepartmentSpecifications.companyIdContains(departmentRequest.getCompanyId()))
        );
    }

    public Long count (Department department) {
        return departmentRepository.count(Specification
                .where(DepartmentSpecifications.companyIdContains(department.getCompanyId()))
        );
    }

    public List<Department> save (List<Department> departments) {
        return departmentRepository.saveAll(departments);
    }

    public void delete (List<Department> departments) {
        departmentRepository.deleteAll(departments);
    }

    public boolean isNotCompanyUser(String loginUser, Integer companyId) {
        Optional<User> userOpt = userRepository.findOne(Specification
                .where(UserSpecifications.emailContains(loginUser))
        );
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return !user.getCompanyId().equals(companyId);
        } else {
            return true;
        }
    }
}
