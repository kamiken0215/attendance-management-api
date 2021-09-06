package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AttendanceClassSpecifications {

    public static Specification<AttendanceClass> companyIdContains(Integer companyId) {
        return (root, query, cb) ->
                cb.equal(root.<Integer>get("companyId"),companyId);
    }

    public static Specification<AttendanceClass> attendanceClassCodeContains(String attendanceClassCode) {
        return !StringUtils.hasText(attendanceClassCode) ? null : (Specification<AttendanceClass>) (root, query, cb) ->
                cb.equal(root.get("attendanceClassCode"),attendanceClassCode);
    }


}
