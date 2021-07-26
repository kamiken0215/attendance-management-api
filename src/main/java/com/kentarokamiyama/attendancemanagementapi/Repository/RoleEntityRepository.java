package com.kentarokamiyama.attendancemanagementapi.Repository;

import com.kentarokamiyama.attendancemanagementapi.entitiy.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleEntityRepository extends JpaRepository<RoleEntity,Integer> {

    RoleEntity findByName(String name);
}
