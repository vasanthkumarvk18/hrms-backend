package com.hrms.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeCreateRequest {
	
	@NotBlank
    private String firstName;

    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String mobile;

    @NotNull
    private Long departmentId;

    @NotNull
    private Long designationId;

    @NotNull
    private LocalDate dateOfJoining;

}
