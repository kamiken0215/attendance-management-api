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

    private final String EXECUTE_SQL = "---実行SQL------------------------------------------------";

    public List<AttendanceClass> find (AttendanceClass attendanceClass) {

        log.severe(EXECUTE_SQL);
        return attendanceClassRepository.findAll(Specification
                .where(AttendanceClassSpecifications.companyIdContains(attendanceClass.getCompanyId()))
                .and(AttendanceClassSpecifications.attendanceClassCodeContains(attendanceClass.getAttendanceClassCode()))
                ,Sort.by(Sort.Direction.ASC,"companyId").and(Sort.by(Sort.Direction.ASC,"attendanceClassCode"))
        );
    }

    public long count (AttendanceClass attendanceClass) {

        log.severe(EXECUTE_SQL);
        return attendanceClassRepository.count(Specification
                .where(AttendanceClassSpecifications.companyIdContains(attendanceClass.getCompanyId()))
        );
    }

    public List<AttendanceClass> save (List<AttendanceClass> attendanceClassList) {
        try {
            log.severe(EXECUTE_SQL);
            return attendanceClassRepository.saveAll(attendanceClassList);
        } catch (Throwable t) {
            log.severe(t.toString());
            return new ArrayList<>();
        }
    }

    public String delete (AttendanceClass attendanceClass) {
        try {
            log.severe(EXECUTE_SQL);
            attendanceClassRepository.delete(attendanceClass);
            return "";
        } catch (Throwable t) {
            log.severe(t.toString());
            return "error";
        }
    }

    public void deleteAll (List<AttendanceClass> attendanceClass) {
        attendanceClassRepository.deleteAll(attendanceClass);
    }
 }
