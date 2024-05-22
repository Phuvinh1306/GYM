package com.hotrodoan.service;

import com.hotrodoan.model.Employee;
import com.hotrodoan.model.GymBranch;
import com.hotrodoan.model.GymBranch_Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GymBranchService {
    public GymBranch createGymBranch(GymBranch gymBranch);
    public GymBranch updateGymBranch(GymBranch gymBranch, Long id);
    public GymBranch getGymBranchById(Long id);
    public Page<GymBranch> getAllGymBranches(String keyword, Pageable pageable);
    public void deleteGymBranchById(Long id);
    public List<GymBranch_Room> getGymBranchRoomById(Long id);
    boolean existsByManager(Employee manager);
}
