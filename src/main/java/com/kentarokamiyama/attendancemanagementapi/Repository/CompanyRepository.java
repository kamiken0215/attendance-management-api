package com.kentarokamiyama.attendancemanagementapi.Repository;

import com.kentarokamiyama.attendancemanagementapi.entitiy.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Integer> {
    Company findById(int id);
}
