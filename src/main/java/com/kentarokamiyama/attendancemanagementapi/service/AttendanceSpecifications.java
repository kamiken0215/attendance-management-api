package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
public class AttendanceSpecifications {

    public static Specification<Attendance> companyIdContains(Integer companyId) {
        return (root, query, cb) ->
                cb.equal(root.<Integer>get("companyId"),companyId);
    }

    public static Specification<Attendance> departmentCodeContains(String departmentCode) {
        return (root, query, cb) ->
                cb.equal(root.<Integer>get("departmentCode"),departmentCode);
    }

    public static Specification<Attendance> attendanceClassCodeContains(String attendanceClassCode) {
        return !StringUtils.hasText(attendanceClassCode) ? null : (Specification<Attendance>) (root, query, cb) ->
                cb.equal(root.get("attendanceClassCode"),attendanceClassCode);
    }

    public static Specification<Attendance> attendanceStatusCodeContains(String attendanceStatusCode) {
        return !StringUtils.hasText(attendanceStatusCode) ? null : (Specification<Attendance>) (root, query, cb) ->
                cb.equal(root.get("attendanceStatusCode"),attendanceStatusCode);
    }

    public static Specification<Attendance> userIdContains(Integer userId) {
        return (root, query, cb) ->
                cb.equal(root.<Integer>get("userId"),userId);
    }

    public static Specification<Attendance> attendanceDateContains(String attendanceDate) {
        return !StringUtils.hasText(attendanceDate) ? null : (Specification<Attendance>) (root, query, cb) ->
                cb.like(root.get("attendanceDate"),attendanceDate + "%");
    }

}
