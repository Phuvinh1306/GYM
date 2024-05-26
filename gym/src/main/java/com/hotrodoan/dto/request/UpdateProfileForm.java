package com.hotrodoan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileForm {
    private String name;
    private String phone;
    private String address;
    private String cccd;
    private String sex;
    private Date createdAt;
}
