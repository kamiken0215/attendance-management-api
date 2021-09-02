package com.kentarokamiyama.attendancemanagementapi.entitiy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "company_name")
    private String companyName;

    @OneToMany(mappedBy = "company")
    private Set<User> user;

//    @ManyToOne
//    @JoinColumn(name = "company_id",referencedColumnName = "company_id", insertable = false, updatable = false)
    @OneToMany(mappedBy = "company")
    private Set<Department> department;

    @OneToMany(mappedBy = "company")
    private Set<AttendanceClass> attendanceClass;
}
