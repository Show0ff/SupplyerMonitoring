package com.osypenko.SMTelegramBot.entityies;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column
    private Integer price;
    @Column
    private String deliverySpeed;
    @Column
    private Double rating;
    @Column
    private String comment;
}
