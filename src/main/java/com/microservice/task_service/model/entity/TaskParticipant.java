package com.microservice.task_service.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "task_participants",
    uniqueConstraints = @UniqueConstraint(columnNames = {"task_id", "user_id"})
)
public class TaskParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false, foreignKey = @ForeignKey(name = "fk_task_participant_task"))
    private Task task;

   
    @Column(name = "user_id", nullable = false)
    private Long userId;


    @Column(name = "added_at", nullable = true)
    private LocalDateTime addedAt = LocalDateTime.now();
    
}
