package com.kentarokamiyama.attendancemanagementapi.service;

import com.kentarokamiyama.attendancemanagementapi.entitiy.AttendanceStatus;
import com.kentarokamiyama.attendancemanagementapi.repository.AttendanceStatusRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class AttendanceStatusService {

    @Autowired
    private AttendanceStatusRepository attendanceStatusRepository;


    private final String EXECUTE_SQL = "---実行SQL------------------------------------------------";

    public List<AttendanceStatus> find () {

        log.severe(EXECUTE_SQL);
        return attendanceStatusRepository.findAll(
                Sort.by(Sort.Direction.ASC,"attendanceStatusCode")
        );
    }

 }
