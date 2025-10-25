package com.swe307.second_project.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO implements Serializable {
    private Integer empno;
    private String ename;
    private String job;
    private LocalDate hiredate;
    private Integer sal;
    private Integer comm;
    private String managerName;
    private String deptName;
    private String imageUrl;
}
