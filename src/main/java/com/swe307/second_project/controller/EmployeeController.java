package com.swe307.second_project.controller;

import com.swe307.second_project.dtos.EmployeeDTO;
import com.swe307.second_project.entity.Department;
import com.swe307.second_project.entity.Employee;
import com.swe307.second_project.repository.DepartmentRepository;
import com.swe307.second_project.service.EmployeeService;
import com.swe307.second_project.service.S3ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentRepository departmentRepository;
    private final S3ClientService s3ClientService;


    @GetMapping
    public String showEmployees(Model model) {
        List<EmployeeDTO> employeesDTOS = employeeService.getAllEmployees();

        model.addAttribute("employeeDTOS", employeesDTOS);
        return "employees";
    }

    @GetMapping("/{id}")
    public String getEmployee(@PathVariable Integer id, Model model) {
        EmployeeDTO emp = employeeService.getEmployeeById(id);
        model.addAttribute("emp", emp);
        return "employee";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        List<Department> depts = departmentRepository.findAll();
        model.addAttribute("departments", depts);
        List<EmployeeDTO> managers = employeeService.getAllEmployees();
        model.addAttribute("managers", managers);
        return "add";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute("employee") Employee employee,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam(required = false) Integer deptId,
                              @RequestParam(required = false) Integer managerId) {

        if (file != null && !file.isEmpty()) {
            String fileName = s3ClientService.uploadFile(file);
            employee.setImageUrl(fileName);
        }

        if (deptId != null) {
            Department dept = departmentRepository.findById(deptId).orElse(null);
            employee.setDept(dept);
        }

        if (managerId != null) {
            Employee manager = employeeService.getManagerById(managerId);
            employee.setManager(manager);
        }

        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
        model.addAttribute("employeeDTO", employeeDTO);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("managers", employeeService.getAllEmployees());
        return "update";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable("id") Integer id,
                                 @ModelAttribute("employeeDTO") EmployeeDTO updatedEmployeeDTO,
                                 @RequestParam(value = "file", required = false) MultipartFile file) {

        employeeService.updateEmployee(id, updatedEmployeeDTO, file);
        return "redirect:/employees/" + id;
    }



    @GetMapping("/delete/{empno}")
    public String deleteEmployee(@PathVariable int empno) {
        employeeService.deleteEmployee(empno);
        return "redirect:/employees";
    }

}
