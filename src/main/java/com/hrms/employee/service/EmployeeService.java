package com.hrms.employee.service;

import com.hrms.employee.dto.EmployeeCreateDTO;
import com.hrms.employee.entity.Employee;
import com.hrms.employee.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee createEmployee(EmployeeCreateDTO request) {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Employee employee = Employee.builder()
                .employeeCode(generateEmployeeId())

                .firstName(request.getFirstName())
                .lastName(request.getLastName())

                .gender(request.getGender())
                .dob(request.getDob())

                .email(request.getEmail())
                .mobile(request.getMobile())

                .departmentId(request.getDepartmentId())
                .designationId(request.getDesignationId())
                .managerId(request.getManagerId())

                .dateOfJoining(request.getDateOfJoining())
                .employmentType(request.getEmploymentType())

                .maritalStatus(request.getMaritalStatus())
                .bloodGroup(request.getBloodGroup())
                .profilePhotoUrl(request.getProfilePhotoUrl())
                .probationEndDate(request.getProbationEndDate())

                .build();


        return employeeRepository.save(employee);
    }

    public List<Employee> getActiveEmployees() {
        return employeeRepository.findByStatus("ACTIVE");
    }

    public Employee getByEmployeeId(String employeeCode) {
        return employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public void exitEmployee(String employeeCode) {

        Employee emp = getByEmployeeId(employeeCode);

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
