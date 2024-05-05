package com.hotrodoan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy' 'HH:mm:ss")
    private Date startDay;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy' 'HH:mm:ss")
    private Date endDay;

    @PrePersist
    @PreUpdate
    public void ruleDate(){
        if (startDay.after(endDay)){
            throw new RuntimeException("Ngày bắt đầu không thể sau ngày kết thúc");
        }
    }
}
