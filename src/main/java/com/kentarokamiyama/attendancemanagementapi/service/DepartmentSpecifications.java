package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.entitiy.Company;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Department;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
public class DepartmentSpecifications {

    public static Specification<Department> companyIdContains(Integer companyId) {
        return companyId == null ? null : (root, query, cb) ->
                cb.equal(root.<Integer>get("companyId"), companyId);
    }

    public static Specification<Department> departmentCodeContains(String departmentCode) {
        return StringUtils.hasText(departmentCode) ? null : (root, query, cb) ->
                cb.equal(root.get("departmentCode"),departmentCode);
    }
}
