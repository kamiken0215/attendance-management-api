package com.kentarokamiyama.attendancemanagementapi.repository;

import com.kentarokamiyama.attendancemanagementapi.entitiy.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department,Integer>, JpaSpecificationExecutor<Department> {
    @Query(value = "SELECT * FROM Department d WHERE d.company_id = ?1 and d.department_code = ?2", nativeQuery = true)
    Department findByCompanyIdDepartmentCode(Integer companyId, String departmentCode);
    //Department findByDepartmentCode(String departmentCode);
}
