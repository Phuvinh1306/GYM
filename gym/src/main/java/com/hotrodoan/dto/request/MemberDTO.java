package com.hotrodoan.dto.request;

import com.hotrodoan.model.GymBranch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private String name;
    private String username;
    private String password;
    private String email;
    private boolean enabled;

    private String phone;
    private String address;
    private String cccd;
    private String sex;
    private GymBranch gymBranch;
}
