package com.hotrodoan.service;

import com.hotrodoan.model.EquipType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EquipTypeService {
    List<EquipType> getAllEquipType();
    EquipType addEquipType(EquipType equipType);
    EquipType updateEquipType(EquipType equipType, Long id);
    void deleteEquipType(Long id);
    EquipType getEquipType(Long id);

    Page<EquipType> getAllEquipType(Pageable pageable);
    Page<EquipType> findEquipTypesByNameContaining(String name, Pageable pageable);
}
