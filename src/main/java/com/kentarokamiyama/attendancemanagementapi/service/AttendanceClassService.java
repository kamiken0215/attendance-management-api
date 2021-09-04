package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.controller.AttendanceClassRequest;
import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceClass;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.repository.AttendanceClassRepository;
import com.kentarokamiyama.attendancemanagementapi.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class AttendanceClassService {

    @Autowired
    private AttendanceClassRepository attendanceClassRepository;
    @Autowired
    private UserRepository userRepository;

    public List<AttendanceClass> find (AttendanceClass attendanceClass) {
        return attendanceClassRepository.findAll(Specification
                .where(AttendanceClassSpecifications.companyIdContains(attendanceClass.getCompanyId()))
                .and(AttendanceClassSpecifications.attendanceClassCodeContains(attendanceClass.getAttendanceClassCode()))
                ,Sort.by(Sort.Direction.ASC,"companyId").and(Sort.by(Sort.Direction.ASC,"attendanceClassCode"))
        );
    }

    public long count (AttendanceClass attendanceClass) {
        return attendanceClassRepository.count(Specification
                .where(AttendanceClassSpecifications.companyIdContains(attendanceClass.getCompanyId()))
        );
    }

    public List<AttendanceClass> save (List<AttendanceClass> attendanceClassList) {
        try {
            return attendanceClassRepository.saveAll(attendanceClassList);
        } catch (Throwable t) {
            log.severe(t.toString());
            return new ArrayList<>();
        }
    }

    public void delete (List<AttendanceClass> attendanceClass) {
        attendanceClassRepository.deleteAll(attendanceClass);
    }

    public boolean isCompanyUser(String loginUser,Integer companyId) {
        Optional<User> userOpt = userRepository.findOne(Specification
                .where(UserSpecifications.emailContains(loginUser))
        );
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getCompanyId().equals(companyId);
        } else {
            return false;
        }
    }
 }
