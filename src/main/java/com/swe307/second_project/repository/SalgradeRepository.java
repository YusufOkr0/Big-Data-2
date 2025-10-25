package com.swe307.second_project.repository;

import com.swe307.second_project.entity.Salgrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalgradeRepository extends JpaRepository<Salgrade, Integer> {
}
