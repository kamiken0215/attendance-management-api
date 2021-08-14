package com.kentarokamiyama.attendancemanagementapi.controller;

import com.kentarokamiyama.attendancemanagementapi.entitiy.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {
    @NotEmpty
    private Integer companyId;
    private List<Department> departments;

    //  id : [
    //      {
    //      "code" : "code",
    //      "name" : "hoge",
    //      },
    //      ...
    //  ]
    //private Map<Integer, List<Map<String,String>>> departments;

//    private String departmentCode;
//
//    private String departmentName;
}
