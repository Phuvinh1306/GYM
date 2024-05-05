package com.hotrodoan.service.impl;

import com.hotrodoan.exception.EmployeeNotfoundException;
import com.hotrodoan.model.Employee;
import com.hotrodoan.repository.EmployeeRepository;
import com.hotrodoan.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

//    @Override
//    public List<Employee> getAllEmployee() {
//        return employeeRepository.findAll();
//    }

    @Override
    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotfoundException("Không tìm thấy nhân viên"));
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotfoundException("Không tìm thấy nhân viên");
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee updateEmployee(Employee employee, Long id) {
        return employeeRepository.findById(id).map(ep -> {
            ep.setName(employee.getName());
            ep.setDob(employee.getDob());
            ep.setCccd(employee.getCccd());
            ep.setPhone(employee.getPhone());
            ep.setEmail(employee.getEmail());
            ep.setAddress(employee.getAddress());
            ep.setStartWork(employee.getStartWork());
            ep.setSex(employee.getSex());
            ep.setAvatar(employee.getAvatar());
            return employeeRepository.save(ep);
        }).orElseThrow(() -> new EmployeeNotfoundException("Không tìm thấy nhân viên"));
    }

    @Override
    public boolean existsByIdAndPositionId(Long id, Long positionId) {
        return employeeRepository.existsByIdAndPositionId(id, positionId);
    }

    @Override
    public Page<Employee> getAllEmployee(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Page<Employee> findEmployeesByNameContaining(String name, Pageable pageable) {
        return employeeRepository.findByNameContaining(name, pageable);
    }

    @Override
    public List<Employee> findEmployeesByPositionId(Long positionId) {
        return employeeRepository.findByPositionId(positionId);
    }
}
