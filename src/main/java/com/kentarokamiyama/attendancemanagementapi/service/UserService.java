package com.kentarokamiyama.attendancemanagementapi.service;
import com.kentarokamiyama.attendancemanagementapi.controller.UserRequest;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.repository.CompanyRepository;
import com.kentarokamiyama.attendancemanagementapi.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log
public class UserService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> find(User user) {

        return userRepository.findAll(Specification
                .where(UserSpecifications.companyIdContains(user.getCompanyId()))
                .and(UserSpecifications.departmentCodeContains(user.getDepartmentCode()))
                .and(UserSpecifications.userIdContains(user.getUserId()))
                .and(UserSpecifications.userNameContains(user.getUserName()))
                .and(UserSpecifications.emailContains(user.getEmail()))
                .and(UserSpecifications.roleCodeContains(user.getRoleCode()))
        );
    }

    public Optional<User> findById(Integer userId) {
        return userRepository.findById(userId);
    }

    public long count (List<Integer> users) {
        return userRepository.findAllById(users).size();
    }

    public Object save (User user) {
        try {
            return userRepository.save(user);
        } catch (Throwable t) {
           log.severe(t.toString());
           if (t.getCause().toString().contains("ConstraintViolationException")) {
               return "メールアドレスを他のものに変更してください";
           } else {
               return "再度時間を置いてから実行してください";
           }
        }
    }

    public String delete (User user) {
        try {
            userRepository.delete(user);
            return "";
        }catch (Throwable t) {
            log.severe(t.toString());
            if (t.toString().contains("DataIntegrityViolationException")) {
                return "ユーザーID:"+ user.getUserId() + " ユーザー名:" + user.getUserName() +"に関連するデータを消してください";
            }
            return "ユーザーID:"+ user.getUserId() + " ユーザー名:" + user.getUserName();
        }
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

    public User findOne (User user) {
        Optional<User> userOpt = userRepository.findOne(Specification
                .where(UserSpecifications.companyIdContains(user.getCompanyId()))
                .and(UserSpecifications.departmentCodeContains(user.getDepartmentCode()))
                .and(UserSpecifications.userIdContains(user.getUserId()))
                .and(UserSpecifications.userNameContains(user.getUserName()))
                .and(UserSpecifications.emailContains(user.getEmail()))
                .and(UserSpecifications.roleCodeContains(user.getRoleCode()))
        );
        return userOpt.orElse(null);
    }
}
