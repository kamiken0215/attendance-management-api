package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceStatus;
import com.kentarokamiyama.attendancemanagementapi.entitiy.Role;
import com.kentarokamiyama.attendancemanagementapi.repository.AttendanceStatusRepository;
import com.kentarokamiyama.attendancemanagementapi.repository.RoleRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;


    private final String EXECUTE_SQL = "---実行SQL------------------------------------------------";

    public List<Role> find () {

        log.severe(EXECUTE_SQL);
        return roleRepository.findAll(
                Sort.by(Sort.Direction.ASC,"roleCode")
        );
    }

 }
