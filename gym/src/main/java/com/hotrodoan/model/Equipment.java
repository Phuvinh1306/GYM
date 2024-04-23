package com.hotrodoan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Min(0)
    @Max(50)
    private int quantity;
    private double price;
    private String madein = "Việt Nam";

    private String image;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equip_type_id", referencedColumnName = "id")
    private EquipType equipType;

    @ManyToMany(mappedBy = "equipment", fetch = FetchType.EAGER)
    private Set<Room> rooms = new HashSet<>();
}
