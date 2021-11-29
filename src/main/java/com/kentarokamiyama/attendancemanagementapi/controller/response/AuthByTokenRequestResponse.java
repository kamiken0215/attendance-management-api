package com.kentarokamiyama.attendancemanagementapi.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthByTokenRequestResponse {

    private String token;

    private Boolean ok;
}
