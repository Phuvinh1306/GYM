package com.hotrodoan.repository;

import com.hotrodoan.model.GymBranch;
import com.hotrodoan.model.GymBranch_Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymBranch_RoomRepository extends JpaRepository<GymBranch_Room, Long> {
    List<GymBranch_Room> findByGymBranch(GymBranch gymBranch);
}
