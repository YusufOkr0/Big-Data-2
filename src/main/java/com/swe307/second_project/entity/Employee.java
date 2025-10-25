package com.swe307.second_project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "emp")
public class Employee {

    @Id
    @Column(name = "empno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer empno;

    @Column(name = "ename", length = 50)
    private String ename;

    @Column(name = "job", length = 50)
    private String job;

    @OneToMany(mappedBy = "manager")
    @ToString.Exclude
    private List<Employee> subordinates;

    @Column(name = "hiredate")
    private LocalDate hiredate;

    @Column(name = "sal")
    private Integer sal;

    @Column(name = "comm")
    private Integer comm;

    @JoinColumn(name = "mgr")
    @ToString.Exclude
    @ManyToOne
    private Employee manager;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deptno")
    private Department dept;

    @Column(name = "image_url")
    private String imageUrl;
}

