package com.kentarokamiyama.attendancemanagementapi.Repository;

import com.kentarokamiyama.attendancemanagementapi.entitiy.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByCode(String code);
}
