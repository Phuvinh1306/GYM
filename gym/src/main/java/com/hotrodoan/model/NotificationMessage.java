package com.hotrodoan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
public class NotificationMessage {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    private String recipientToken;

    @Column(nullable = false)
    @Size(min = 1, message = "Title must not be empty")
    private String title;
    @Size(min = 1, message = "Content must not be empty")
    private String body;
    @Lob
    private String image;

    private Map<String, String> data;
}
