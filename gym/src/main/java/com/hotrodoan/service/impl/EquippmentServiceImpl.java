package com.hotrodoan.service.impl;

import com.hotrodoan.exception.EquipmentNotFoundException;
import com.hotrodoan.model.Equipment;
import com.hotrodoan.repository.EquipmentRepository;
import com.hotrodoan.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquippmentServiceImpl implements EquipmentService {
    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    @Override
    public Equipment addEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment updateEquipment(Equipment equipment, Long id) {
        return equipmentRepository.findById(id).map(eqm -> {
            eqm.setName(equipment.getName());
            eqm.setQuantity(equipment.getQuantity());
            eqm.setPrice(equipment.getPrice());
            eqm.setMadein(equipment.getMadein());
            eqm.setImage(equipment.getImage());
            eqm.setEquipType(equipment.getEquipType());
            return equipmentRepository.save(eqm);
        }).orElseThrow(() -> new EquipmentNotFoundException("Không tìm thấy thiết bị"));
    }

    @Override
    public void deleteEquipment(Long id) {
        if (!equipmentRepository.existsById(id)) {
            throw new EquipmentNotFoundException("Không tìm thấy thiết bị");
        }
        equipmentRepository.deleteById(id);
    }

    @Override
    public Equipment getEquipment(Long id) {
        return equipmentRepository.findById(id).orElseThrow(() -> new EquipmentNotFoundException("Không tìm thấy thiết bị"));
    }

    @Override
    public Page<Equipment> getAllEquipment(Pageable pageable) {
        return equipmentRepository.findAll(pageable);
    }

    @Override
    public Page<Equipment> findEquipmentByNameContaining(String name, Pageable pageable) {
        return equipmentRepository.findByNameContaining(name, pageable);
    }
}
