package com.kentarokamiyama.attendancemanagementapi.entitiy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
@Data
public class Role {

    @Id
    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "role_name")
    private String roleName;

    @Column
    private String explanation;
}
