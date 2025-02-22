package com.example.dorametrics.model.changerequest;

import com.example.dorametrics.model.deployment.Deployment;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "change_requests")
public class ChangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime approvedAt;

    @Column
    private LocalDateTime implementedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChangeRequestStatus status;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String requestedBy;

    @Column
    private String prNumber; // Pull Request number if applicable

    @Column
    private String commitId;

    // Nuevos campos para Lead Time
    @Column(name = "lead_time_minutes")
    private Long leadTimeMinutes;

    @Column
    private String branch;

    @Column
    private String repository;

    // Relación con el deployment
    @ManyToOne
    @JoinColumn(name = "deployment_id")
    private Deployment deployment;

    // Metodo para calcular el Lead Time
    public void calculateLeadTime() {
        if (createdAt != null && implementedAt != null) {
            this.leadTimeMinutes = java.time.Duration.between(createdAt, implementedAt).toMinutes();
        }
    }
}