package com.kentarokamiyama.attendancemanagementapi.repository;

import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceClassRepository extends JpaRepository<AttendanceClass,Integer>, JpaSpecificationExecutor<AttendanceClass> {

    AttendanceClass findByCompanyIdAndAttendanceClassCode(Integer companyId,String attendanceClassCode);
}
