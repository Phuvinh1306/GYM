package com.hotrodoan.service;

import com.hotrodoan.model.GymBranch;
import com.hotrodoan.model.GymBranch_Room;

import java.util.List;

public interface GymBranch_RoomService {
    public GymBranch_Room createGymBranch_Room(GymBranch_Room gymBranchRoom);
    public GymBranch_Room updateGymBranch_Room(GymBranch_Room gymBranchRoom, Long id);
    public void deleteGymBranch_Room(Long id);
//    public void removeAllGymBranchesFromRoom(Long roomId);
    public List<GymBranch_Room> getGymBranchesByGymBranch(GymBranch gymBranch);
}
