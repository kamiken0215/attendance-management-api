package com.kentarokamiyama.attendancemanagementapi.Repository;

import com.kentarokamiyama.attendancemanagementapi.entitiy.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,String> {
    Department findByCode(String code);
}
