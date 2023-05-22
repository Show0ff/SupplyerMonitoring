package com.khlopin.SupplierMonitoring.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "corpFile")
public class CorpFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private User uploader;

    @ManyToMany
    @JoinTable(
            name = "file_recipients",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> recipients = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "file_departments",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private List<Department> departmentRecipients = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "file_projects",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projectRecipients = new ArrayList<>();
}

