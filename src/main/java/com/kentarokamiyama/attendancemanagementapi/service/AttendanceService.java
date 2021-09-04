package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.controller.AttendanceRequest;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceView;
import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import com.kentarokamiyama.attendancemanagementapi.repository.AttendanceRepository;
import com.kentarokamiyama.attendancemanagementapi.repository.AttendanceViewRepository;
import com.kentarokamiyama.attendancemanagementapi.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log
public class AttendanceService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private AttendanceViewRepository attendanceViewRepository;

    public List<AttendanceView> find(AttendanceView attendanceView) {
        log.severe("---実行SQL------------------------------------------------");
        try {
            return attendanceViewRepository.findAll(Specification
                    .where(AttendanceViewSpecifications.companyIdContains(attendanceView.getCompanyId()))
                    .and(AttendanceViewSpecifications.departmentCodeContains(attendanceView.getDepartmentCode()))
                    .and(AttendanceViewSpecifications.attendanceClassCodeContains(attendanceView.getAttendanceClassCode()))
                    .and(AttendanceViewSpecifications.attendanceStatusCodeContains(attendanceView.getAttendanceStatusCode()))
                    .and(AttendanceViewSpecifications.userIdContains(attendanceView.getUserId()))
                    .and(AttendanceViewSpecifications.attendanceDateContains(attendanceView.getAttendanceDate()))
                    ,Sort.by(Sort.Direction.ASC,"userId").and(Sort.by(Sort.Direction.ASC,"attendanceDate"))
            );
        } catch (Throwable t) {
            log.severe(t.toString());
            return new ArrayList<>();
        }
    }

    public List<AttendanceView> findAll() {
        try {
            return attendanceViewRepository.findAll();
        } catch (Throwable t) {
            log.severe("-------------------------------------");
            log.severe(t.toString());
            return new ArrayList<AttendanceView>();
        }
    }


//    public List<Attendance> fetchAll(Integer companyId) {
//        return attendanceRepository.fetchAll(companyId,null,
//                null,
//                null,
//                null);
//    }

    public long count (Attendance attendance) {
        return attendanceRepository.count(Specification
                .where(AttendanceSpecifications.userIdContains(attendance.getUserId()))
        );
    }

    public Object save (Attendance attendance) {
        try {
            return attendanceRepository.save(attendance);
        } catch (Throwable t) {
            log.severe(t.toString());
            return "error";
        }
    }

    public String delete(Attendance attendance) {
        try {
            attendanceRepository.delete(attendance);
            return "";
        } catch (Throwable t) {
            log.severe(t.toString());
            return "error";
        }
    }

    public String deleteAll (List<Attendance> attendances) {
        try {
            attendanceRepository.deleteAll(attendances);
            return "";
        } catch (Throwable t) {
            log.severe(t.toString());
            return "削除失敗";
        }
    }

    public boolean isNotExistUser(Integer userId, String loginUser) {
        User user = userRepository.findByUserId(userId);
        return user == null || !user.getEmail().equals(loginUser);
    }
}
