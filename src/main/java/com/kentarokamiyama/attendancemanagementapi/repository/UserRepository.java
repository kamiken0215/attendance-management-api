package com.kentarokamiyama.attendancemanagementapi.repository;

import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> ,JpaSpecificationExecutor<User>{
    User findByEmail(String email);
    User findByUserId(Integer userId);

}
