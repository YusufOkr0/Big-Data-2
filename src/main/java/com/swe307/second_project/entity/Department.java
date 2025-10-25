package com.swe307.second_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "dept")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department  {

    @Id
    @Column(name = "deptno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deptno;

    @Column(name = "dname", length = 50)
    private String dname;

    @Column(name = "loc", length = 50)
    private String loc;

    @OneToMany(mappedBy = "dept", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Employee> employees;
}

