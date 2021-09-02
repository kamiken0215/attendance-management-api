package com.kentarokamiyama.attendancemanagementapi.repository;

import com.kentarokamiyama.attendancemanagementapi.entitiy.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Integer>, JpaSpecificationExecutor<Attendance> {

//    @Query(value = "SELECT u.user_id as user_id, user_name, attendance_date, start_time, end_time, ac.attendance_class_code as attendance_class_code,attendance_class_name, ats.attendance_status_code as attendance_status_code,attendance_status_name, department_name FROM attendance a " +
//            "left outer join user u on\n" +
//            "a.user_id = u.user_id \n" +
//            "LEFT outer join company c on\n" +
//            "u.company_id = c.company_id \n" +
//            "LEFT outer join department d on\n" +
//            "u.company_id = d.company_id \n" +
//            "AND u.department_code = d.department_code \n" +
//            "LEFT OUTER JOIN attendance_status ats on\n" +
//            "a.attendance_status_code = ats.attendance_status_code \n" +
//            "LEFT OUTER JOIN attendance_class ac on\n" +
//            "a.attendance_class_code = ac.attendance_class_code \n" +
//            "and c.company_id = ac.company_id where c.company_id = :companyId \n" +
//            "and d.department_code = :departmentCode \n" +
//            "and a.attendance_status_code = :attendanceStatusCode \n" +
//            "and a.attendance_class_code = :attendanceClassCode \n" +
//            "and a.user_id = :userId",nativeQuery = true)
//    List<Attendance> fetchAll(@Param("companyId") Integer companyId,
//                              @Param("departmentCode") String departmentCode,
//                              @Param("attendanceStatusCode") String attendanceStatusCode,
//                              @Param("attendanceClassCode") String attendanceClassCode,
//                              @Param("userId") Integer userId);
//
//    @Query(value = "SELECT u.user_id as user_id, user_name, attendance_date, start_time, end_time, ac.attendance_class_code as attendance_class_code,attendance_class_name, ats.attendance_status_code as attendance_status_code,attendance_status_name, department_name FROM attendance a " +
//            "left outer join user u on\n" +
//            "a.user_id = u.user_id \n" +
//            "LEFT outer join company c on\n" +
//            "u.company_id = c.company_id \n" +
//            "LEFT outer join department d on\n" +
//            "u.company_id = d.company_id \n" +
//            "AND u.department_code = d.department_code \n" +
//            "LEFT OUTER JOIN attendance_status ats on\n" +
//            "a.attendance_status_code = ats.attendance_status_code \n" +
//            "LEFT OUTER JOIN attendance_class ac on\n" +
//            "a.attendance_class_code = ac.attendance_class_code \n" +
//            "and c.company_id = ac.company_id where c.company_id = ?1 AND department_code = ?2",nativeQuery = true)
//    List<Attendance> fetchByCompanyIdAndDepartmentCode(Integer companyId, String departmentCode);
//
//    @Query(value = "SELECT u.user_id as user_id, user_name, attendance_date, start_time, end_time, ac.attendance_class_code as attendance_class_code,attendance_class_name, ats.attendance_status_code as attendance_status_code,attendance_status_name, department_name FROM attendance a " +
//            "left outer join user u on\n" +
//            "a.user_id = u.user_id \n" +
//            "LEFT outer join company c on\n" +
//            "u.company_id = c.company_id \n" +
//            "LEFT outer join department d on\n" +
//            "u.company_id = d.company_id \n" +
//            "AND u.department_code = d.department_code \n" +
//            "LEFT OUTER JOIN attendance_status ats on\n" +
//            "a.attendance_status_code = ats.attendance_status_code \n" +
//            "LEFT OUTER JOIN attendance_class ac on\n" +
//            "a.attendance_class_code = ac.attendance_class_code \n" +
//            "and c.company_id = ac.company_id where c.company_id = ?1 AND department_code = ?2",nativeQuery = true)
//    List<Attendance> fetchByCompanyIdDepartmentCodeAndUserId(Integer companyId);
}
