package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
public class UserSpecifications {

    public static Specification<User> userIdContains(Integer userId) {
        return userId == null ? null : (Specification<User>) (root, query, cb) ->
                cb.equal(root.<Integer>get("userId"),userId);
    }

    public static Specification<User> userNameContains(String userName) {
        return !StringUtils.hasText(userName) ? null : (Specification<User>) (root, query, cb) ->
                cb.like(root.get("userName"),"%"+userName+"%");
    }

    public static Specification<User> companyIdContains(Integer companyId) {
        return companyId == null ? null : (Specification<User>) (root, query, cb) ->
                cb.equal(root.<Integer>get("companyId"),companyId);
    }

    public static Specification<User> departmentCodeContains(String departmentCode) {
        return !StringUtils.hasText(departmentCode) ? null : (Specification<User>) (root, query, cb) ->
                cb.equal(root.get("departmentCode"),departmentCode);
    }

    public static Specification<User> roleCodeContains(String roleCode) {
        return !StringUtils.hasText(roleCode) ? null : (Specification<User>) (root, query, cb) ->
                cb.equal(root.get("roleCode"),roleCode);
    }

    public static Specification<User> emailContains(String email) {
        return !StringUtils.hasText(email) ? null : (Specification<User>) (root, query, cb) ->
                cb.equal(root.get("email"),email);
    }

}
