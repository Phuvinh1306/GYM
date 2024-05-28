package com.hotrodoan.dto.response;

import com.hotrodoan.model.GymBranch;
import com.hotrodoan.model.Package;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseProfile {
    private String name;
    private String username;
    private String phone;
    private String email;
    private String address;
    private String cccd;
    private String sex;
    private Date createdAt;
    private GymBranch gymBranch;
    private List<Package> packs;
}
