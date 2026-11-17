package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    // In-memory storage keeps this scenario self-contained (no database required).
    private final ConcurrentMap<Long, Employee> employees = new ConcurrentHashMap<>();

    // (b) Add a new employee.
    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
        if (employee.getId() == null || employee.getId() <= 0) {
            return ResponseEntity.badRequest().body("Employee ID must be a positive number.");
        }
        if (employee.getName() == null || employee.getName().isBlank()) {
            return ResponseEntity.badRequest().body("Employee name is required.");
        }
        if (employee.getDepartment() == null || employee.getDepartment().isBlank()) {
            return ResponseEntity.badRequest().body("Employee department is required.");
        }
        if (!Double.isFinite(employee.getSalary()) || employee.getSalary() < 0) {
            return ResponseEntity.badRequest().body("Salary must be a valid non-negative number.");
        }

        Employee existing = employees.putIfAbsent(employee.getId(), employee);
        if (existing != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("An employee with ID " + employee.getId() + " already exists.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    // (c1) Retrieve all employees.
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> result = new ArrayList<>(employees.values());
        result.sort(Comparator.comparing(Employee::getId));
        return ResponseEntity.ok(result);
    }

    // (c2) Retrieve one employee by ID.
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        Employee employee = employees.get(id);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee with ID " + id + " was not found.");
        }
        return ResponseEntity.ok(employee);
    }

    // (d) Update all employee details for the given ID.
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id,
                                            @RequestBody Employee updatedEmployee) {
        if (updatedEmployee.getName() == null || updatedEmployee.getName().isBlank()) {
            return ResponseEntity.badRequest().body("Employee name is required.");
        }
        if (updatedEmployee.getDepartment() == null || updatedEmployee.getDepartment().isBlank()) {
            return ResponseEntity.badRequest().body("Employee department is required.");
        }
        if (!Double.isFinite(updatedEmployee.getSalary()) || updatedEmployee.getSalary() < 0) {
            return ResponseEntity.badRequest().body("Salary must be a valid non-negative number.");
        }

        Employee existing = employees.get(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee with ID " + id + " was not found.");
        }

        Employee employee = new Employee(id, updatedEmployee.getName(),
                updatedEmployee.getDepartment(), updatedEmployee.getSalary());
        employees.put(id, employee);
        return ResponseEntity.ok(employee);
    }

    // (e) Delete an employee by ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        Employee removed = employees.remove(id);
        if (removed == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee with ID " + id + " was not found.");
        }
        return ResponseEntity.ok("Employee with ID " + id + " deleted successfully.");
    }
}
