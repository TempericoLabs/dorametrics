package com.example.dorametrics.service;

import com.example.dorametrics.model.changerequest.ChangeRequest;
import com.example.dorametrics.model.changerequest.ChangeRequestStatus;
import com.example.dorametrics.repository.ChangeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChangeRequestService {
    private final ChangeRequestRepository repository;

    public ChangeRequest registerChangeRequest(ChangeRequest changeRequest) {
        if (changeRequest.getCreatedAt() == null) {
            changeRequest.setCreatedAt(LocalDateTime.now());
        }
        if (changeRequest.getStatus() == null) {
            changeRequest.setStatus(ChangeRequestStatus.PENDING);
        }
        return repository.save(changeRequest);
    }

    public ChangeRequest updateStatus(Long id, ChangeRequestStatus newStatus) {
        ChangeRequest changeRequest = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ChangeRequest not found"));

        changeRequest.setStatus(newStatus);

        if (newStatus == ChangeRequestStatus.IMPLEMENTED) {
            changeRequest.setImplementedAt(LocalDateTime.now());
            changeRequest.calculateLeadTime();
        } else if (newStatus == ChangeRequestStatus.APPROVED) {
            changeRequest.setApprovedAt(LocalDateTime.now());
        }

        return repository.save(changeRequest);
    }

    public Map<String, Object> calculateLeadTimeMetrics(LocalDateTime startDate, LocalDateTime endDate) {
        List<ChangeRequest> implementedChanges = repository
                .findByStatusAndImplementedAtBetween(ChangeRequestStatus.IMPLEMENTED, startDate, endDate);

        Map<String, Object> metrics = new HashMap<>();

        // Promedio de Lead Time
        Double averageLeadTime = repository.getAverageLeadTimeMinutes();
        metrics.put("averageLeadTimeMinutes", averageLeadTime != null ? averageLeadTime : 0.0);

        // Total de cambios implementados
        metrics.put("totalImplementedChanges", implementedChanges.size());

        // Distribución de Lead Time
        Map<String, Long> leadTimeDistribution = new HashMap<>();
        leadTimeDistribution.put("under1Hour", implementedChanges.stream()
                .filter(cr -> cr.getLeadTimeMinutes() != null && cr.getLeadTimeMinutes() < 60).count());
        leadTimeDistribution.put("1to24Hours", implementedChanges.stream()
                .filter(cr -> cr.getLeadTimeMinutes() != null && cr.getLeadTimeMinutes() >= 60 && cr.getLeadTimeMinutes() < 1440).count());
        leadTimeDistribution.put("over24Hours", implementedChanges.stream()
                .filter(cr -> cr.getLeadTimeMinutes() != null && cr.getLeadTimeMinutes() >= 1440).count());
        metrics.put("leadTimeDistribution", leadTimeDistribution);

        return metrics;
    }

    public List<ChangeRequest> getAllChangeRequests() {
        return repository.findAll();
    }

    public ChangeRequest getChangeRequestById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ChangeRequest not found"));
    }

    public void deleteChangeRequest(Long id) {
        repository.deleteById(id);
    }

    public List<ChangeRequest> getChangeRequestsByStatus(ChangeRequestStatus status) {
        return repository.findByStatus(status);
    }

    public List<ChangeRequest> findByCommitId(String commitId) {
        return repository.findByCommitId(commitId);
    }
}