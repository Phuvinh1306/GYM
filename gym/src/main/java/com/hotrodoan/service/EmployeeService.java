package com.hotrodoan.service;

import com.hotrodoan.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    Employee addEmployee(Employee employee);
//    List<Employee> getAllEmployee();
    Employee getEmployee(Long id);
    void deleteEmployee(Long id);
    Employee updateEmployee(Employee employee, Long id);

    boolean existsByIdAndPositionId(Long id, Long positionId);

    Page<Employee> getAllEmployee(Pageable pageable);
    Page<Employee> findEmployeesByNameContaining(String name, Pageable pageable);
}
