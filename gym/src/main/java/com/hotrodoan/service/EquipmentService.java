package com.hotrodoan.service;

import com.hotrodoan.model.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EquipmentService {
    List<Equipment> getAllEquipment();
    Equipment addEquipment(Equipment equipment);
    Equipment updateEquipment(Equipment equipment, Long id);
    void deleteEquipment(Long id);
    Equipment getEquipment(Long id);

    Page<Equipment> getAllEquipment(Pageable pageable);
    Page<Equipment> findEquipmentByNameContaining(String name, Pageable pageable);
}
