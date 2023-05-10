package com.khlopin.SupplierMonitoring.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "userName")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "corp_id")
    private Integer corpId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Project project;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Task> manageTasks = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.GUEST;


}
