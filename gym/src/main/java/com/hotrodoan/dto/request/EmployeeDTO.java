package com.hotrodoan.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotrodoan.model.Image;
import com.hotrodoan.model.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private String name;
    private String username;
    private String password;
    private String email;
    private boolean enabled=true;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy' 'HH:mm:ss")
    private Date dob;
    private String cccd;
    private String phone;
    private String address;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy' 'HH:mm:ss")
    private Date startWork;
    private String sex;
    private Position position;
    private Image image;
}
