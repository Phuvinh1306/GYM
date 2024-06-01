package com.hotrodoan.service;

import com.hotrodoan.model.Employee;
import com.hotrodoan.model.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface EmployeeService {
    Employee addEmployee(Employee employee);
    Employee addEmployee(String name, String username, String passord, String email, String dob, String cccd,
                         String phone, String address, String startWork, String sex, Position position, MultipartFile file) throws Exception;
//    List<Employee> getAllEmployee();
    Employee getEmployee(Long id);
    void deleteEmployee(Long id);
    Employee updateEmployee(String name, String username, String email, String dob, String cccd,
                            String phone, String address, String startWork, String sex, Position position,
                            MultipartFile file, Long id) throws Exception;

    boolean existsByIdAndPositionId(Long id, Long positionId);

    Page<Employee> getAllEmployee(Pageable pageable);
    Page<Employee> findEmployeesByNameContaining(String name, Pageable pageable);
    List<Employee> findEmployeesByPositionId(Long positionId);
    Page<Employee> getByEmployeesByPositionNameContaining(String positionName, Pageable pageable);
    List<Employee> getManagerAvailable();
}
