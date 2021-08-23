package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.controller.AttendanceRequest;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.repository.AttendanceRepository;
import com.kentarokamiyama.attendancemanagementapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<Attendance> find(Attendance attendance) {
        return attendanceRepository.findAll(Specification
                .where(AttendanceSpecifications.userIdContains(attendance.getUserId()))
                .and(AttendanceSpecifications.attendanceDateContains(attendance.getAttendanceDate()))
        );
    }

    public long count (Attendance attendance) {
        return attendanceRepository.count(Specification
                .where(AttendanceSpecifications.userIdContains(attendance.getUserId()))
        );
    }

    public Attendance save (Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public void deleteAll (List<Attendance> attendances) {
        attendanceRepository.deleteAll(attendances);
    }

    public boolean isNotExistUser(Integer userId, String loginUser) {
        User user = userRepository.findByUserId(userId);
        return user == null || !user.getEmail().equals(loginUser);
    }
}
