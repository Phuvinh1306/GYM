package com.hotrodoan.service.impl;

import com.hotrodoan.exception.PositionNotFoundException;
import com.hotrodoan.model.Position;
import com.hotrodoan.repository.PositionRepository;
import com.hotrodoan.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionRepository positionRepository;

    @Override
    public Position addPosition(Position position) {
        return positionRepository.save(position);
    }

    @Override
    public List<Position> getAllPosition() {
        return positionRepository.findAll();
    }

    @Override
    public Position getPosition(Long id) {
        return positionRepository.findById(id).orElseThrow(() -> new PositionNotFoundException("Không tìm thấy chức vụ"));
    }

    @Override
    public void deletePosition(Long id) {
        if (!positionRepository.existsById(id)) {
            throw new PositionNotFoundException("Không tìm thấy chức vụ");
        }
        positionRepository.deleteById(id);
    }

    @Override
    public Position updatePosition(Position position, Long id) {
        return positionRepository.findById(id).map(po -> {
            po.setName(position.getName());
            return positionRepository.save(po);
        }).orElseThrow(() -> new PositionNotFoundException("Không tìm thấy chức vụ"));
    }
}
