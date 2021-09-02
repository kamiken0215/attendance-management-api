package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceView;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AttendanceViewSpecifications {

    public static Specification<AttendanceView> companyIdContains(Integer companyId) {
        return (root, query, cb) ->
                cb.equal(root.<Integer>get("companyId"),companyId);
    }

    public static Specification<AttendanceView> departmentCodeContains(String departmentCode) {
        return !StringUtils.hasText(departmentCode) ? null : (Specification<AttendanceView>) (root, query, cb) ->
                cb.equal(root.get("departmentCode"),departmentCode);
    }

    public static Specification<AttendanceView> attendanceClassCodeContains(String attendanceClassCode) {
        return !StringUtils.hasText(attendanceClassCode) ? null : (Specification<AttendanceView>) (root, query, cb) ->
                cb.equal(root.get("attendanceClassCode"),attendanceClassCode);
    }

    public static Specification<AttendanceView> attendanceStatusCodeContains(String attendanceStatusCode) {
        return !StringUtils.hasText(attendanceStatusCode) ? null : (Specification<AttendanceView>) (root, query, cb) ->
                cb.equal(root.get("attendanceStatusCode"),attendanceStatusCode);
    }

    public static Specification<AttendanceView> userIdContains(Integer userId) {
        return userId == null ? null : (Specification<AttendanceView>) (root, query, cb) ->
                cb.equal(root.<Integer>get("userId"),userId);
    }

    public static Specification<AttendanceView> attendanceDateContains(String attendanceDate) {
        return !StringUtils.hasText(attendanceDate) ? null : (Specification<AttendanceView>) (root, query, cb) ->
                cb.like(root.get("attendanceDate"),attendanceDate + "%");
    }

}
