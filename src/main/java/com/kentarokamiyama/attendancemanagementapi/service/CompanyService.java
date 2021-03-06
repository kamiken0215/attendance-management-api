package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.controller.request.CompanyRequest;
import com.kentarokamiyama.attendancemanagementapi.entitiy.*;
import com.kentarokamiyama.attendancemanagementapi.repository.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private AttendanceClassRepository attendanceClassRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<Company> find(CompanyRequest companyRequest) {
        return companyRepository.findAll(Specification
                .where(CompanySpecifications.companyIdContains(companyRequest.getCompanyId()))
        );
    }

    public Company findOne(Integer companyId) {
        Optional<Company> opt = companyRepository.findById(companyId);
        return opt.orElse(null);
    }

    public Object save(Company company) {
        try {
            return companyRepository.save(company);
        } catch (Throwable t) {
            log.severe(t.toString());
            return "不正なデータです";
        }
    }

    public void delete (Company company) {
        companyRepository.delete(company);
    }

    public boolean isExistUser (Integer userId,String loginUser) {
        Optional<User> userOpt = userRepository.findOne(Specification
                .where(UserSpecifications.userIdContains(userId))
                .and(UserSpecifications.emailContains(loginUser))
        );
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getEmail().equals(loginUser);
        } else {
            return false;
        }
    }

    public boolean isNotExistCompany(String loginUser, Integer companyId) {
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
