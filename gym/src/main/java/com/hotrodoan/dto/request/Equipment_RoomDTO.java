package com.hotrodoan.dto.request;

import com.hotrodoan.model.EquipType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment_RoomDTO {
    private String name;
    private List<Equipment_Amount> equipmentAmounts;
}
