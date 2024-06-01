package com.hotrodoan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 3, max = 150)
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy' 'HH:mm:ss")
    private Date dob;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "\\d{12}", message = "CCCD phải bao gồm 12 chữ số")
    private String cccd;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "(\\d{4}[-.]?\\d{3}[-.]?\\d{3})", message = "Số điện thoại phải bao gồm 10 chữ số và có thể có dấu chấm hoặc dấu gạch ngang giữa các phần tử")
    private String phone;

    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy' 'HH:mm:ss")
    private Date startWork;

    private String sex;
    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image avatar;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
