package com.swe307.second_project.service.Impl;

import com.swe307.second_project.dtos.EmployeeDTO;
import com.swe307.second_project.entity.Department;
import com.swe307.second_project.entity.Employee;
import com.swe307.second_project.repository.DepartmentRepository;
import com.swe307.second_project.repository.EmployeeRepository;
import com.swe307.second_project.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EmployeeService {

    @Value("${aws.object.url}")
    private String AWS_OBJECT_URL;

    private final EmployeeRepository employeeRepository;
    private final StorageService storageService;
    private final DepartmentRepository departmentRepository;


    @Cacheable(value = "employees")
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(this::employeeDTOMapper).toList();
    }

    @Cacheable(value = "employee", key = "#empno", unless = "#result == null")            // redisKey = employee::empno
    public EmployeeDTO getEmployeeById(Integer empno) {
        Employee employee = employeeRepository.findById(empno)
                .orElseThrow(() -> new RuntimeException("Employee not found with empno: " + empno));

        return employeeDTOMapper(employee);
    }

    @CachePut(value = "employee", key = "#employee.empno", unless = "#result == null")
    @CacheEvict(value = "employees", allEntries = true)
    public EmployeeDTO saveEmployee(Employee employee) {
        Employee existing = employeeRepository.save(employee);
        return employeeDTOMapper(existing);
    }


    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "employee", key = "#empno"),
            @CacheEvict(value = "employees", allEntries = true)
    })
    public void deleteEmployee(Integer empno) {
        Employee employee = employeeRepository.findById(empno)
                .orElseThrow(() -> new RuntimeException("Employee not found with empno: " + empno));

        employeeRepository.clearManager(empno);
        storageService.deleteFileFromStorage(employee.getImageUrl());
        employeeRepository.deleteById(empno);
    }


    @CachePut(value = "employee", key = "#empno")
    @CacheEvict(value = "employees", allEntries = true)
    public EmployeeDTO updateEmployee(Integer empno, EmployeeDTO dto, MultipartFile file) {
        Employee existing = employeeRepository.findById(empno)
                .orElseThrow(() -> new RuntimeException("Employee not found with empno: " + empno));

        existing.setEname(dto.getEname());
        existing.setJob(dto.getJob());
        existing.setHiredate(dto.getHiredate());
        existing.setSal(dto.getSal());
        existing.setComm(dto.getComm());

        if (dto.getDeptName() != null && !dto.getDeptName().isBlank()) {
            Department dept = departmentRepository.findByDname(dto.getDeptName())
                    .orElse(null);
            existing.setDept(dept);
        }

        if (dto.getManagerName() != null && !dto.getManagerName().isBlank()) {
            Employee manager = employeeRepository.findByEname(dto.getManagerName())
                    .orElse(null);
            existing.setManager(manager);
        }

        if (file != null && !file.isEmpty()) {
            if (existing.getImageUrl() != null) {
                storageService.deleteFileFromStorage(existing.getImageUrl());
            }
            String newUrl = storageService.uploadFile(file);
            existing.setImageUrl(newUrl);
        }

        employeeRepository.save(existing);

        return employeeDTOMapper(existing);
    }


    public Employee getManagerById(Integer empno) {
        return employeeRepository.findById(empno)
                .orElseThrow(() -> new RuntimeException("Employee not found with empno: " + empno));

    }

    private EmployeeDTO employeeDTOMapper(Employee existing) {
        String deptName = existing.getDept() == null ? null : existing.getDept().getDname();
        String managerName = existing.getManager() == null ? null : existing.getManager().getEname();
        return EmployeeDTO.builder()
                .empno(existing.getEmpno())
                .ename(existing.getEname())
                .job(existing.getJob())
                .hiredate(existing.getHiredate())
                .sal(existing.getSal())
                .comm(existing.getComm())
                .managerName(managerName)
                .deptName(deptName)
                .imageUrl(AWS_OBJECT_URL + existing.getImageUrl())
                .build();
    }
}
