package com.hotrodoan.dto.request;

import com.hotrodoan.model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room_Amount {
    Room room;
    int amount;
}
