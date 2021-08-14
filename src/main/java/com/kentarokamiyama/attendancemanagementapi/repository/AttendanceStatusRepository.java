package com.kentarokamiyama.attendancemanagementapi.repository;

import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceStatusRepository extends JpaRepository<AttendanceStatus,Integer> {

    AttendanceStatus findByAttendanceStatusCode(String attendanceStatusCode);
}
