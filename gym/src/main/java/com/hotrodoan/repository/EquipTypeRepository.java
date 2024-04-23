package com.hotrodoan.repository;

import com.hotrodoan.model.EquipType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipTypeRepository extends JpaRepository<EquipType, Long> {
    Page<EquipType> findAll(Pageable pageable);
    Page<EquipType> findByNameContaining(String name, Pageable pageable);
}
