package com.hrms.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeResponse {

    private String employeeId;
    private String firstName;
    private String email;
    private String status;
}
