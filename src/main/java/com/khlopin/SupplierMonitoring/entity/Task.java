package com.khlopin.SupplierMonitoring.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User worker;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User manager;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Project project;


    private String text;


    private String extraInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private TaskStatus taskStatus;

//    @Column(name = "finish_task_until")
//    private LocalDateTime needToFinishTaskUntil;
}
