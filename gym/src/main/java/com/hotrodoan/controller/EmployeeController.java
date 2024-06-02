package com.hotrodoan.controller;

import com.hotrodoan.dto.request.EmployeeDTO;
import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.*;
import com.hotrodoan.service.EmployeeService;
import com.hotrodoan.service.RoleService;
import com.hotrodoan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

//    @GetMapping("")
//    public ResponseEntity<Page<Employee>> getAllEmployee(@RequestParam(defaultValue = "0") int page,
//                                                         @RequestParam(defaultValue = "10") int size,
//                                                         @RequestParam(defaultValue = "id") String sortBy,
//                                                         @RequestParam(defaultValue = "desc") String order) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
//        return new  ResponseEntity<>(employeeService.getAllEmployee(pageable), HttpStatus.OK);
//    }

    @PostMapping("/admin/add")
    public ResponseEntity<Employee> addEmployee(@RequestParam String name,
                                                   @RequestParam String username,
                                                   @RequestParam String password,
                                                   @RequestParam String email,
                                                   @RequestParam String dob,
                                                   @RequestParam String cccd,
                                                   @RequestParam String phone,
                                                   @RequestParam String address,
                                                   @RequestParam String startWork,
                                                   @RequestParam String sex,
                                                   @RequestParam Position  position,
                                                   @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {

        Employee newEmployee = employeeService.addEmployee(name, username, password, email, dob, cccd, phone, address,
                startWork, sex, position, file);
        return new ResponseEntity<>(newEmployee, HttpStatus.OK);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Employee> updateEmployee(@RequestParam String name,
                                                      @RequestParam String username,
                                                      @RequestParam String email,
                                                      @RequestParam String dob,
                                                      @RequestParam String cccd,
                                                      @RequestParam String phone,
                                                      @RequestParam String address,
                                                      @RequestParam String startWork,
                                                      @RequestParam String sex,
                                                      @RequestParam Position position,
                                                      @RequestParam(value = "file", required = false) MultipartFile file,
                                                      @PathVariable Long id) throws Exception{
        Employee employee = employeeService.updateEmployee(name, username, email, dob, cccd, phone,
                address, startWork, sex, position, file, id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable("id") Long id) {
        return new ResponseEntity<>(employeeService.getEmployee(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Page<Employee>> findEmployeesByNameContaining(@RequestParam(defaultValue = "") String positionName,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size,
                                                                       @RequestParam(defaultValue = "id") String sortBy,
                                                                       @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        Page<Employee> employees = employeeService.getByEmployeesByPositionNameContaining(positionName, pageable);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/admin/manager-available")
    public ResponseEntity<List<Employee>> getManagerAvailable() {
        List<Employee> employees = employeeService.getManagerAvailable();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}
