package com.kentarokamiyama.attendancemanagementapi.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {

    private Integer companyId;

    private String companyName;
}
