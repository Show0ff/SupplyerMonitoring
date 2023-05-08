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

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private User worker;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @Column(name = "text")
    private String text;

    @Column(name = "extra_info")
    private String extraInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private TaskStatus taskStatus;

//    @Column(name = "finish_task_until")
//    private LocalDateTime needToFinishTaskUntil;
}
