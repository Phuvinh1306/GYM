package com.hotrodoan.service.impl;

import com.hotrodoan.model.GymBranch;
import com.hotrodoan.model.GymBranch_Room;
import com.hotrodoan.repository.GymBranchRepository;
import com.hotrodoan.repository.GymBranch_RoomRepository;
import com.hotrodoan.service.GymBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GymBranchServiceImpl implements GymBranchService {
    @Autowired
    private GymBranchRepository gymBranchRepository;
    @Autowired
    private GymBranch_RoomRepository gymBranch_roomRepository;

    @Override
    public GymBranch createGymBranch(GymBranch gymBranch) {
        return gymBranchRepository.save(gymBranch);
    }

    @Override
    public GymBranch updateGymBranch(GymBranch gymBranch, Long id) {
        return gymBranchRepository.findById(id).map(gb -> {
            gb.setName(gymBranch.getName());
            gb.setAddress(gymBranch.getAddress());
            gb.setManager(gymBranch.getManager());
            return gymBranchRepository.save(gb);
        }).orElseThrow(() -> new RuntimeException("Gym branch not found"));
    }

    @Override
    public GymBranch getGymBranchById(Long id) {
        return gymBranchRepository.findById(id).orElseThrow(() -> new RuntimeException("Gym branch not found"));
    }

    @Override
    public Page<GymBranch> getAllGymBranches(String name, String address, Pageable pageable) {

        return gymBranchRepository.findByNameOrAddressContaining(name, address, pageable);
    }

    @Override
    public void deleteGymBranchById(Long id) {
        gymBranchRepository.deleteById(id);
    }

    @Override
    public List<GymBranch_Room> getGymBranchRoomById(Long id) {
        GymBranch gymBranch = gymBranchRepository.findById(id).orElse(null);
        List<GymBranch_Room> gymBranch_rooms = gymBranch_roomRepository.findByGymBranch(gymBranch);
        return gymBranch_rooms;
    }
}
