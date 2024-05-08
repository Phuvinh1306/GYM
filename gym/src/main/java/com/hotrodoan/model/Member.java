package com.hotrodoan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 150)
    private String name;

    @Column(unique = true)
    @Pattern(regexp = "(\\d{4}[-.]?\\d{3}[-.]?\\d{3})", message = "Số điện thoại phải bao gồm 10 chữ số và có thể có dấu chấm hoặc dấu gạch ngang giữa các phần tử")
    private String phone;

    @Column(unique = true)
    @Pattern(regexp = "\\d{12}", message = "CCCD phải bao gồm 12 chữ số")
    private String cccd;

    private String sex;

//    @ManyToMany(mappedBy = "member")
//    private Set<Member_Package> memberPackages = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<History> histories;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = new Date();
    }
}
