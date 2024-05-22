package com.hotrodoan.dto.request;

import com.hotrodoan.model.Equipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment_Amount {
    Equipment equipment;
    int amount;
}
