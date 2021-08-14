package com.kentarokamiyama.attendancemanagementapi.service;
import com.kentarokamiyama.attendancemanagementapi.controller.UserRequest;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.repository.CompanyRepository;
import com.kentarokamiyama.attendancemanagementapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> find(UserRequest user) {

        return userRepository.findAll(Specification
                .where(UserSpecifications.companyIdContains(user.getCompanyId()))
                .and(UserSpecifications.departmentCodeContains(user.getDepartmentCode()))
                .and(UserSpecifications.userIdContains(user.getUserId()))
                .and(UserSpecifications.userNameContains(user.getUserName()))
                .and(UserSpecifications.emailContains(user.getEmail()))
                .and(UserSpecifications.roleCodeContains(user.getRoleCode()))
        );
    }

    public long count (List<Integer> users) {
        return userRepository.findAllById(users).size();
    }

    public User save (User user) {
        return userRepository.save(user);
    }

    public void delete (User user) {
        List<User> result = userRepository.findAll(Specification
                .where(UserSpecifications.companyIdContains(user.getCompanyId()))
                .and(UserSpecifications.departmentCodeContains(user.getDepartmentCode()))
                .and(UserSpecifications.userIdContains(user.getUserId()))
        );
        userRepository.deleteAll(result);
    }

    public boolean isNotExistUser(Integer userId, String loginUser) {
        Optional<User> userOpt = userRepository.findOne(Specification
                .where(UserSpecifications.userIdContains(userId))
                .and(UserSpecifications.emailContains(loginUser))
        );
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return !user.getEmail().equals(loginUser);
        } else {
            return true;
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
