package com.hotrodoan.controller;

import com.hotrodoan.dto.request.EmployeeDTO;
import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.Employee;
import com.hotrodoan.model.Role;
import com.hotrodoan.model.RoleName;
import com.hotrodoan.model.User;
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

import java.sql.Timestamp;
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
    public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        User user = new User();
        user.setName(employeeDTO.getName());
        user.setUsername(employeeDTO.getUsername());
        user.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        user.setEmail(employeeDTO.getEmail());
        Set<Role> roles = new HashSet<>();

        Employee employee = new Employee();
        employee.setName(employeeDTO.getFullName());
        employee.setDob(employeeDTO.getDob());
        employee.setCccd(employeeDTO.getCccd());
        employee.setPhone(employeeDTO.getPhone());
        employee.setAddress(employeeDTO.getAddress());
        employee.setStartWork(employeeDTO.getStartWork());
        employee.setSex(employeeDTO.getSex());
        employee.setPosition(employeeDTO.getPosition());
        employee.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        if (employeeDTO.getPosition().getId() == 3){
            Role managerRole = roleService.findByName(RoleName.MANAGER).orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(managerRole);
        }
        else {
            Role pmRole = roleService.findByName(RoleName.EMPLOYEE).orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(pmRole);
        }

        user.setRoles(roles);
        User newUser = userService.save(user);
        employee.setUser(newUser);
        Employee newEmployee = employeeService.addEmployee(employee);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable Long id){
        Employee employee = employeeService.getEmployee(id);
        User user = employee.getUser();
        user.setName(employeeDTO.getName());
        Set<Role> roles = new HashSet<>();

        employee.setName(employeeDTO.getFullName());
        employee.setDob(employeeDTO.getDob());
        employee.setPhone(employeeDTO.getPhone());
        employee.setAddress(employeeDTO.getAddress());
        if (employeeDTO.getPosition().getId() == 3){
            Role managerRole = roleService.findByName(RoleName.MANAGER).orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(managerRole);
        }
        else {
            Role pmRole = roleService.findByName(RoleName.EMPLOYEE).orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(pmRole);
        }

        user.setRoles(roles);
        User updatedUser = userService.save(user);
        employee.setUser(updatedUser);
        employeeService.updateEmployee(employee, id);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable("id") Long id) {
        Employee employee = employeeService.getEmployee(id);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName(employee.getUser().getName());
        employeeDTO.setUsername(employee.getUser().getUsername());
        employeeDTO.setEmail(employee.getUser().getEmail());
        employeeDTO.setFullName(employee.getName());
        employeeDTO.setDob(employee.getDob());
        employeeDTO.setCccd(employee.getCccd());
        employeeDTO.setPhone(employee.getPhone());
        employeeDTO.setAddress(employee.getAddress());
        employeeDTO.setStartWork(employee.getStartWork());
        employeeDTO.setSex(employee.getSex());
        employeeDTO.setPosition(employee.getPosition());
        Set<String> roles = new HashSet<>();
        roles.add(employee.getUser().getRoles().toString());
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
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
}
