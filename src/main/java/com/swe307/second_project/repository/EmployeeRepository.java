package com.swe307.second_project.repository;

import com.swe307.second_project.entity.Employee;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEname(String ename);

    @Modifying
    @Query("UPDATE Employee e SET e.manager = null WHERE e.manager.empno = :empno")
    void clearManager(@Param("empno") Integer empno);

}
