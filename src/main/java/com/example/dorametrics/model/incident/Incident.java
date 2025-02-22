// src/main/java/com/example/dorametrics/model/incident/Incident.java
package com.example.dorametrics.model.incident;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "incidents")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private String severity; // P1, P2, P3, etc.

    @Column
    private String impact; // High, Medium, Low

    @Column
    private String assignedTo;

    @Column
    private String relatedDeploymentId; // Si el incidente está relacionado con un despliegue

    @Column
    private Long timeToResolveMinutes; // Para la métrica Time to Restore Service
}