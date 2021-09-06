package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.controller.DepartmentRequest;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Department;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.repository.DepartmentRepository;
import com.kentarokamiyama.attendancemanagementapi.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Department> find (Department department) {
        return departmentRepository.findAll(Specification
                .where(DepartmentSpecifications.companyIdContains(department.getCompanyId()))
                .and(DepartmentSpecifications.departmentCodeContains(department.getDepartmentCode()))
                ,Sort.by(Sort.Direction.ASC,"companyId").and(Sort.by(Sort.Direction.ASC,"departmentCode"))
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

    public String delete (Department department) {
        try {
            departmentRepository.delete(department);
            return "";
        } catch (Throwable t) {
            log.severe(t.toString());
            if (t.toString().contains("DataIntegrityViolationException")) {
                return "部門コード:"+ department.getDepartmentCode() + " 部門名:" + department.getDepartmentName() +"に関連するデータを消してください";
            }
            return "部門コード:"+ department.getDepartmentCode() + " 部門名:" + department.getDepartmentName();
        }
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
