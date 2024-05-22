package com.hotrodoan.dto.request;

import com.hotrodoan.model.EquipType;
import com.hotrodoan.model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room_EquipmentDTO {
    private String name;
    private int quantity;
    private double price;
    private String madein = "Việt Nam";
    private String image;
    private EquipType equipType;
    private List<Room_Amount> roomAmounts;
}
