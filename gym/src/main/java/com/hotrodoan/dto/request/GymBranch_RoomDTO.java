package com.hotrodoan.dto.request;

import com.hotrodoan.model.Employee;
import com.hotrodoan.model.Equipment;
import com.hotrodoan.model.GymBranch;
import com.hotrodoan.model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GymBranch_RoomDTO {
    private String branchGymName;
    private String address;
    private Employee manager;
    private List<Room_Amount> roomAndAmounts;
    private int totalMember=0;
}
