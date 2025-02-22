package com.example.dorametrics.repository;

import com.example.dorametrics.model.changerequest.ChangeRequest;
import com.example.dorametrics.model.changerequest.ChangeRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChangeRequestRepository extends JpaRepository<ChangeRequest, Long> {

    // Buscar por estado
    List<ChangeRequest> findByStatus(ChangeRequestStatus status);

    // Buscar cambios en un rango de fechas
    List<ChangeRequest> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // Obtener el tiempo promedio de entrega (Lead Time)
    @Query("SELECT AVG(cr.leadTimeMinutes) FROM ChangeRequest cr WHERE cr.status = 'IMPLEMENTED'")
    Double getAverageLeadTimeMinutes();

    // Buscar por commit ID
    List<ChangeRequest> findByCommitId(String commitId);

    // Buscar cambios implementados en un período
    List<ChangeRequest> findByStatusAndImplementedAtBetween(
            ChangeRequestStatus status,
            LocalDateTime start,
            LocalDateTime end
    );
}