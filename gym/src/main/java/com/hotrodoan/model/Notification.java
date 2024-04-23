package com.hotrodoan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, message = "Title must not be empty")
    private String title;
    @Size(min = 1, message = "Content must not be empty")
    private String content;
    @Lob
    private String fileName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date sentDate = new Date();
}
