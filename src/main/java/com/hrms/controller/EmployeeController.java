package com.hrms.controller;

import com.hrms.dto.EmployeeCreateRequest;
import com.hrms.dto.EmployeeResponse;
import com.hrms.entity.Employee;
import com.hrms.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@CrossOrigin
public class EmployeeController {
	
	private final EmployeeService employeeService;
	
	@PostMapping
    public EmployeeResponse createEmployee(
            @Valid @RequestBody EmployeeCreateRequest request) {

        Employee emp = employeeService.createEmployee(request);

        return mapToResponse(emp);
    }
	
	@GetMapping
    public List<EmployeeResponse> getEmployees() {

        return employeeService.getActiveEmployees()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
	
	@GetMapping("/all")
	public List<EmployeeResponse> getAllEmployees() {

	    return employeeService.getAllEmployees()
	            .stream()
	            .map(this::mapToResponse)
	            .collect(Collectors.toList());
	}
	
	@GetMapping("/{employeeId}")
    public EmployeeResponse getByEmployeeId(@PathVariable String employeeId) {

        Employee emp = employeeService.getByEmployeeId(employeeId);

        return mapToResponse(emp);
    }
	
	@PutMapping("/{employeeId}/exit")
    public String exitEmployee(@PathVariable String employeeId) {

        employeeService.exitEmployee(employeeId);

        return "Employee exited successfully";
    }
	
	private EmployeeResponse mapToResponse(Employee emp) {

        return EmployeeResponse.builder()
                .employeeId(emp.getEmployeeId())
                .firstName(emp.getFirstName())
                .email(emp.getEmail())
                .status(emp.getStatus())
                .build();
    }


}
