package com.hotrodoan.util;

import com.hotrodoan.model.Position;
import com.hotrodoan.repository.PositionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class PositionInsert {
    @Bean
    public CommandLineRunner demo(PositionRepository positionRepository) {
        return (args) -> {
            if (positionRepository.findByName("Admin") == null) {
                Position position = new Position();
                position.setName("Admin");
                positionRepository.save(position);
            }

            if (positionRepository.findByName("Personal Trainer") == null) {
                Position position = new Position();
                position.setName("Personal Trainer");
                positionRepository.save(position);
            }


            if (positionRepository.findByName("Manager") == null) {
                Position position = new Position();
                position.setName("Manager");
                positionRepository.save(position);
            }
        };
    }
}
