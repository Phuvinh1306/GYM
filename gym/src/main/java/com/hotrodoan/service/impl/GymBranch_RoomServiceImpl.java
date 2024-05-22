package com.hotrodoan.service.impl;

import com.hotrodoan.model.GymBranch;
import com.hotrodoan.model.GymBranch_Room;
import com.hotrodoan.repository.GymBranch_RoomRepository;
import com.hotrodoan.service.GymBranch_RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GymBranch_RoomServiceImpl implements GymBranch_RoomService {
    @Autowired
    private GymBranch_RoomRepository gymBranchRoomRepository;

    @Override
    public GymBranch_Room createGymBranch_Room(GymBranch_Room gymBranchRoom) {
        return gymBranchRoomRepository.save(gymBranchRoom);
    }

    @Override
    public GymBranch_Room updateGymBranch_Room(GymBranch_Room gymBranchRoom, Long id) {
        return gymBranchRoomRepository.findById(id).map(gb->{
            gb.setGymBranch(gymBranchRoom.getGymBranch());
            gb.setRoom(gymBranchRoom.getRoom());
            gb.setAmount(gymBranchRoom.getAmount());
            return gymBranchRoomRepository.save(gb);
        }).orElseThrow(()->new RuntimeException("Gym branch room not found"));
    }

    @Override
    public void deleteGymBranch_Room(Long id) {
        gymBranchRoomRepository.deleteById(id);
    }

    @Override
    public List<GymBranch_Room> getGymBranchesByGymBranch(GymBranch gymBranch) {
        return gymBranchRoomRepository.findByGymBranch(gymBranch);
    }
}
