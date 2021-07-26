package com.kentarokamiyama.attendancemanagementapi.Repository;

import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);
}
