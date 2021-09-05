package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.entitiy.Company;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
public class CompanySpecifications {

    public static Specification<Company> companyIdContains(Integer companyId) {
        return companyId == null ? null : (root, query, cb) ->
                cb.equal(root.<Integer>get("companyId"),companyId);
    }

    public static Specification<Company> companyNameContains(String companyName) {
        return !StringUtils.hasText(companyName) ? null : (root, query, cb) ->
                cb.equal(root.get("companyName"),companyName);
    }

}
