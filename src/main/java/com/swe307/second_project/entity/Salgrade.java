package com.swe307.second_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "salgrade")
public class Salgrade {

    @Id
    @Column(name = "grade")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer grade;

    @Column(name = "losal")
    private Integer losal;

    @Column(name = "hisal")
    private Integer hisal;
}
