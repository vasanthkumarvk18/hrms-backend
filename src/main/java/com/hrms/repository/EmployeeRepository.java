package com.hrms.repository;
import com.hrms.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface EmployeeRepository extends JpaRepository<Employee,Long>{
	
	Optional<Employee>findByEmail(String email);
	
	boolean existsByEmail(String email);
	
	Optional<Employee>findByEmployeeId(String employeeId);
	
	List<Employee>findByStatus(String status);

}
