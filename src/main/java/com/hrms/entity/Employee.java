package com.hrms.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(
	    name = "employees",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = "email"),
	        @UniqueConstraint(columnNames = "employee_id")
	    }
	)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "employee_id", nullable = false, length = 20)
    private String employeeId;
	
	@Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
	
	@Column(name = "last_name", length = 50)
    private String lastName;
	
	@Column(nullable = false, length = 100)
    private String email;
	
	@Column(nullable = false, length = 15)
    private String mobile;
	
	@Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "designation_id", nullable = false)
    private Long designationId;

    @Column(name = "manager_id")
    private Long managerId;
    
    @Column(name = "date_of_joining", nullable = false)
    private LocalDate dateOfJoining;

    @Column(name = "employment_type", nullable = false, length = 20)
    private String employmentType; 

    @Column(nullable = false, length = 20)
    private String status; 
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
