package com.hotrodoan.service.impl;

import com.hotrodoan.exception.EquipTypeNotFoundException;
import com.hotrodoan.model.EquipType;
import com.hotrodoan.repository.EquipTypeRepository;
import com.hotrodoan.service.EquipTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipTypeServiceImpl implements EquipTypeService {
    @Autowired
    private EquipTypeRepository equipTypeRepository;

    @Override
    public List<EquipType> getAllEquipType() {
        return equipTypeRepository.findAll();
    }

    @Override
    public EquipType addEquipType(EquipType equipType) {
        return equipTypeRepository.save(equipType);
    }

    @Override
    public EquipType updateEquipType(EquipType equipType, Long id) {
        return equipTypeRepository.findById(id).map(eq -> {
            eq.setName(equipType.getName());
            return equipTypeRepository.save(eq);
        }).orElseThrow(() -> new EquipTypeNotFoundException("Không tìm thấy loại thiết bị"));
    }

    @Override
    public void deleteEquipType(Long id) {
        if (!equipTypeRepository.existsById(id)) {
            throw new EquipTypeNotFoundException("Không tìm thấy loại thiết bị");
        }
        equipTypeRepository.deleteById(id);
    }

    @Override
    public EquipType getEquipType(Long id) {
        return equipTypeRepository.findById(id).orElseThrow(() -> new EquipTypeNotFoundException("Không tìm thấy loại thiết bị"));
    }

    @Override
    public Page<EquipType> getAllEquipType(Pageable pageable) {
        return equipTypeRepository.findAll(pageable);
    }

    @Override
    public Page<EquipType> findEquipTypesByNameContaining(String name, Pageable pageable) {
        return equipTypeRepository.findByNameContaining(name, pageable);
    }
}
