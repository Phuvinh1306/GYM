package com.hotrodoan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseImage {
    private String fileName;
    private String downloadUrl;
    private String fileType;
    private Long fileSize;
}
