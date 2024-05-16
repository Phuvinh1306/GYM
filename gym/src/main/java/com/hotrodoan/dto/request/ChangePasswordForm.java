package com.hotrodoan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordForm {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
