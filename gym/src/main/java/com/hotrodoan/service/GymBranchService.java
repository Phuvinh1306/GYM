package com.hotrodoan.service;

import com.hotrodoan.model.GymBranch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GymBranchService {
    public GymBranch createGymBranch(GymBranch gymBranch);
    public GymBranch updateGymBranch(GymBranch gymBranch, Long id);
    public GymBranch getGymBranchById(Long id);
    public Page<GymBranch> getAllGymBranches(String name, String address, Pageable pageable);
    public void deleteGymBranchById(Long id);
}
