package com.hotrodoan.repository;

import com.hotrodoan.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByIdAndPositionId(Long id, Long positionId);
    Page<Employee> findAll(Pageable pageable);
    Page<Employee> findByNameContaining(String name, Pageable pageable);
    List<Employee> findByPositionId(Long positionId);
    Page<Employee> findByPosition_NameContaining(String positionName, Pageable pageable);
    @Query("SELECT e FROM Employee e LEFT JOIN GymBranch g ON e.id = g.manager.id WHERE e.position.id = 3 AND g.manager.id IS NULL")
    List<Employee> findAvailableManagers();
}
