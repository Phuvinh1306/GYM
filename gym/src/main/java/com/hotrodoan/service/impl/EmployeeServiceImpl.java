package com.hotrodoan.service.impl;

import com.hotrodoan.exception.EmployeeNotfoundException;
import com.hotrodoan.model.*;
import com.hotrodoan.repository.EmployeeRepository;
import com.hotrodoan.repository.UserRepository;
import com.hotrodoan.service.EmployeeService;
import com.hotrodoan.service.ImageService;
import com.hotrodoan.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private RoleService roleService;

    @Override
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee addEmployee(String name, String username, String passord, String email, String dob, String cccd,
                                String phone, String address, String startWork, String sex, Position position,
                                MultipartFile file) throws Exception {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passord);
        user.setEmail(email);
        user.setEnabled(true);
        Set<Role> roles = new HashSet<>();
        if (position.getId() == 3){
            Role managerRole = roleService.findByName(RoleName.MANAGER).orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(managerRole);
        }
        else {
            Role pmRole = roleService.findByName(RoleName.EMPLOYEE).orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(pmRole);
        }
        user.setRoles(roles);
        User newUser = userRepository.save(user);
        Employee employee = new Employee();
        employee.setUser(newUser);
        employee.setName(name);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date dobDate = formatter.parse(dob);
            Date startWorkDate = formatter.parse(startWork);
            employee.setDob(dobDate);
            employee.setStartWork(startWorkDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        employee.setCccd(cccd);
        employee.setPhone(phone);
        employee.setAddress(address);
        employee.setSex(sex);
        employee.setPosition(position);
        employee.setCreatedAt(new Date());

        if (file != null && !file.isEmpty()) {
            Image image = imageService.saveImage(file);
            employee.setAvatar(image);
        }

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
    public Employee updateEmployee(String name, String username, String email, String dob, String cccd,
                                   String phone, String address, String startWork, String sex, Position position,
                                   MultipartFile file, Long id) throws Exception {
        return employeeRepository.findById(id).map(ep -> {
            User user = ep.getUser();
            user.setName(name);
            user.setUsername(username);
            user.setEmail(email);
            Set<Role> roles = new HashSet<>();
            if (position.getId() == 3){
                Role managerRole = roleService.findByName(RoleName.MANAGER).orElseThrow(() -> new RuntimeException("Role not found"));
                roles.add(managerRole);
            }
            else {
                Role pmRole = roleService.findByName(RoleName.EMPLOYEE).orElseThrow(() -> new RuntimeException("Role not found"));
                roles.add(pmRole);
            }
            user.setRoles(roles);
            User updatedUser = userRepository.save(user);
            ep.setUser(updatedUser);
            ep.setName(name);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            try {
                Date dobDate = formatter.parse(dob);
                Date startWorkDate = formatter.parse(startWork);
                ep.setDob(dobDate);
                ep.setStartWork(startWorkDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ep.setCccd(cccd);
            ep.setPhone(phone);
            ep.setAddress(address);
            ep.setSex(sex);
            ep.setPosition(position);
            Employee updatedEmployee = new Employee();
            if (file != null && !file.isEmpty()) {
                String oldAvatarId = null;
                if (ep.getAvatar() != null && !ep.getAvatar().equals("")) {
                    oldAvatarId = ep.getAvatar().getId();
                }

                try {
                    Image image = imageService.saveImage(file);
                    ep.setAvatar(image);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                updatedEmployee = employeeRepository.save(ep);

                if (oldAvatarId != null) {
                    try {
                        imageService.deleteImage(oldAvatarId);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return updatedEmployee;
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

    @Override
    public Page<Employee> getByEmployeesByPositionNameContaining(String positionName, Pageable pageable) {
        return employeeRepository.findByPosition_NameContaining(positionName, pageable);
    }

    @Override
    public List<Employee> getManagerAvailable() {
        return employeeRepository.findAvailableManagers();
    }
}
