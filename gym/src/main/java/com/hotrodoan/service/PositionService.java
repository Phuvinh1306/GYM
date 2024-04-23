package com.hotrodoan.service;

import com.hotrodoan.model.Position;

import java.util.List;

public interface PositionService {
    Position addPosition(Position position);
    List<Position> getAllPosition();
    Position getPosition(Long id);
    void deletePosition(Long id);
    Position updatePosition(Position position, Long id);
}
