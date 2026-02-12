package com.hrms.attendance.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hrms.attendance.entity.*;
import com.hrms.employee.entity.*;
import com.hrms.attendance.repository.*;

import java.time.Duration;


@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    // CHECK-IN
    public Attendance checkIn(Employee employee) {

//    	LocalDate today = LocalDate.now();

        attendanceRepository
        .findByEmployeeAndDateAndCheckOutIsNull(employee, LocalDate.now())
        .ifPresent(a -> {
            throw new RuntimeException("Already checked in. Please check out first.");
        });
        
        int nextSession = attendanceRepository
                .findTopByEmployeeAndDateOrderBySessionNoDesc(employee, LocalDate.now())
                .map(a -> a.getSessionNo() + 1)
                .orElse(1);
        
        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setDate(LocalDate.now());
        attendance.setCheckIn(LocalTime.now());
        attendance.setSessionNo(nextSession);
        attendance.setStatus("PRESENT");

        return attendanceRepository.save(attendance);
    }
    
    // CHECK_OUT
    public Attendance checkOut(Employee employee) {

        Attendance attendance = attendanceRepository
            .findByEmployeeAndDateAndCheckOutIsNull(employee, LocalDate.now())
            .orElseThrow(() ->
                new RuntimeException("No check-in found for today")
            );

        if (attendance.getCheckOut() != null) {
            throw new RuntimeException("Already checked out");
        }

        attendance.setCheckOut(LocalTime.now());
        attendance.setStatus("COMPLETED");

        return attendanceRepository.save(attendance);
    }

    

    // GET attendance by employee
    public List<Attendance> getAttendance(Employee employee) {
        return attendanceRepository.findByEmployee(employee);
    }
    
    //ONE DAY WORKING TIME
    public long calculateTotalMinutes(Employee employee, LocalDate date) {

        List<Attendance> sessions =
                attendanceRepository.findByEmployeeAndDate(employee, date);

        long totalMinutes = 0;

        for (Attendance a : sessions) {
            if (a.getCheckIn() != null && a.getCheckOut() != null) {
                totalMinutes +=
                    java.time.Duration.between(
                        a.getCheckIn(),
                        a.getCheckOut()
                    ).toMinutes();
            }
        }

        return totalMinutes;
    }
      
    public String calculateDayStatus(Employee employee, LocalDate date) {

        long minutes = calculateTotalMinutes(employee, date);

        if (minutes >= 480) {          // 8 hours
            return "FULL_DAY";
        } else if (minutes >= 240) {   // 4 hours
            return "HALF_DAY";
        } else {
            return "ABSENT";
        }
    }
    
    public Map<String, Object> getMonthlySummary(
            Employee employee,
            int year,
            int month) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Attendance> list =
                attendanceRepository.findByEmployeeAndDateBetween(
                        employee, start, end);

        long totalMinutes = 0;
        int fullDays = 0;
        int halfDays = 0;

        Map<LocalDate, Long> dailyMinutes = new HashMap<>();

        for (Attendance a : list) {
            if (a.getCheckIn() != null && a.getCheckOut() != null) {
                long minutes =
                    Duration.between(a.getCheckIn(), a.getCheckOut()).toMinutes();
                dailyMinutes.merge(a.getDate(), minutes, Long::sum);
            }
        }

        for (long minutes : dailyMinutes.values()) {
            totalMinutes += minutes;
            if (minutes >= 480) fullDays++;
            else if (minutes >= 240) halfDays++;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("fullDays", fullDays);
        result.put("halfDays", halfDays);
        result.put("absentDays",
                start.lengthOfMonth() - (fullDays + halfDays));
        result.put("totalHours", totalMinutes / 60.0);

        return result;
    }


    
//    public Attendance save(Attendance attendance) {
//
//    	attendanceRepository.findByEmployeeIdAndDate(
//    			attendance.getEmployee().getId(),
//                attendance.getDate()
//        ).ifPresent(a -> {
//            throw new RuntimeException(
//                "Attendance already marked for today"
//            );
//        });
//
//        return attendanceRepository.save(attendance);
//    }

}
