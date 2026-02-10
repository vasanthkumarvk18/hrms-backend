package com.hrms.attendance.controller;

import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;


import org.springframework.web.bind.annotation.*;

import com.hrms.attendance.entity.*;
import com.hrms.employee.entity.*;
import com.hrms.employee.repository.*;
import com.hrms.attendance.service.*;

@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = "http://localhost:4200") // Add CORS support for Angular
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeRepository employeeRepository;

    public AttendanceController(
            AttendanceService attendanceService,
            EmployeeRepository employeeRepository) {
        this.attendanceService = attendanceService;
        this.employeeRepository = employeeRepository;
    }

    // CHECK-IN API
    @PostMapping("/check-in/{employeeCode}")
    public Attendance checkIn(@PathVariable String employeeCode) {

        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return attendanceService.checkIn(employee);
    }
    
    // CHECK_OUT API
    @PostMapping("/check-out/{employeeCode}")
    public Attendance checkOut(@PathVariable String employeeCode) {
    	Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
    			.orElseThrow(() -> new RuntimeException("Employee not found" + employeeCode));
        return attendanceService.checkOut(employee);
    }


    // GET attendance by employee
    @GetMapping("/{employeeCode}")
    public List<Attendance> getAttendance(@PathVariable String employeeCode) {

        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return attendanceService.getAttendance(employee);
    }
    
 // ONE DAY ATTENDANCE SUMMARY
    @GetMapping("/summary")
    public Map<String, Object> getDailySummary(
            @RequestParam String employeeCode,
            @RequestParam LocalDate date
    ) {
        Employee emp = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found"+ employeeCode));

        long minutes = attendanceService.calculateTotalMinutes(emp, date);
        String status = attendanceService.calculateDayStatus(emp, date);

        Map<String, Object> result = new HashMap<>();
        result.put("totalMinutes", minutes);
        result.put("totalHours", minutes / 60.0);
        result.put("status", status);

        return result;
    }

    
    @GetMapping("/monthly-summary")
    public Map<String, Object> getMonthlySummary(
            @RequestParam String employeeCode,
            @RequestParam int year,
            @RequestParam int month) {

        Employee emp = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found with code: " + employeeCode));

        return attendanceService.getMonthlySummary(emp, year, month);
    }


}
