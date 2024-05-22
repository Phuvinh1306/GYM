package com.hotrodoan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member_Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "package_id", referencedColumnName = "id")
    private Package pack;

    private int quantity=1;
    private int amount;

    private Date startDate;
    private Date endDate;

    // Setter method for startDate
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//        // Update endDate based on startDate and duration
//        if (startDate != null && pack != null && pack.getDuration() > 0) {
//            LocalDate localStartDate = startDate.toLocalDate();
//            LocalDate localEndDate = localStartDate.plusDays(pack.getDuration());
//            this.endDate = Date.valueOf(localEndDate);
//        }
//    }
//
//    // Setter method for Package
//    public void setPack(Package pack) {
//        this.pack = pack;
//        // Update endDate based on startDate and duration
//        if (startDate != null && pack != null && pack.getDuration() > 0) {
//            LocalDate localStartDate = startDate.toLocalDate();
//            LocalDate localEndDate = localStartDate.plusDays(pack.getDuration());
//            this.endDate = Date.valueOf(localEndDate);
//        }
//    }
}
