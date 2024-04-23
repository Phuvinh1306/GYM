package com.hotrodoan.repository;

import com.hotrodoan.model.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Page<Equipment> findAll(Pageable pageable);
    Page<Equipment> findByNameContaining(String name, Pageable pageable);
}
