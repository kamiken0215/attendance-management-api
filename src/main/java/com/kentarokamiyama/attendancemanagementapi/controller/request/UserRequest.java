package com.kentarokamiyama.attendancemanagementapi.controller.request;

import com.kentarokamiyama.attendancemanagementapi.entitiy.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotEmpty
    private Integer companyId;

    private List<User> users;
}
