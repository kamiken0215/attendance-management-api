package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AttendanceClassSpecifications {

    public static Specification<AttendanceClass> companyIdContains(Integer companyId) {
        return (root, query, cb) ->
                cb.equal(root.<Integer>get("companyId"),companyId);
    }

    public static Specification<AttendanceClass> attendanceClassCodeContains(String classCode) {
        return (root, query, cb) ->
                cb.equal(root.get("classCode"),classCode);
    }


}
