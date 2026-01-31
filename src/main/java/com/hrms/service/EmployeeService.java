package com.hrms.service;

import com.hrms.dto.EmployeeCreateRequest;
import com.hrms.entity.Employee;
import com.hrms.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee createEmployee(EmployeeCreateRequest request) {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Employee employee = Employee.builder()
                .employeeId(generateEmployeeId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .departmentId(request.getDepartmentId())
                .designationId(request.getDesignationId())
                .dateOfJoining(request.getDateOfJoining())
                .employmentType("FULL_TIME") // default value
                .build();

        return employeeRepository.save(employee);
    }

    public List<Employee> getActiveEmployees() {
        return employeeRepository.findByStatus("ACTIVE");
    }

    public Employee getByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public void exitEmployee(String employeeId) {

        Employee emp = getByEmployeeId(employeeId);

        emp.setStatus("EXITED");

        employeeRepository.save(emp);
    }

    private String generateEmployeeId() {
        return "EMP" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }
    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
}
