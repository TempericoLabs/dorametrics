package com.example.dorametrics.controller;

import com.example.dorametrics.model.changerequest.ChangeRequest;
import com.example.dorametrics.model.changerequest.ChangeRequestStatus;
import com.example.dorametrics.service.ChangeRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/changes")
@RequiredArgsConstructor
public class ChangeRequestController {

    private final ChangeRequestService changeRequestService;

    @PostMapping
    public ResponseEntity<ChangeRequest> createChangeRequest(@RequestBody ChangeRequest changeRequest) {
        return ResponseEntity.ok(changeRequestService.registerChangeRequest(changeRequest));
    }

    @GetMapping
    public ResponseEntity<List<ChangeRequest>> getAllChangeRequests() {
        return ResponseEntity.ok(changeRequestService.getAllChangeRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChangeRequest> getChangeRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(changeRequestService.getChangeRequestById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ChangeRequest> updateStatus(
            @PathVariable Long id,
            @RequestParam ChangeRequestStatus status) {
        return ResponseEntity.ok(changeRequestService.updateStatus(id, status));
    }

    @GetMapping("/metrics/leadtime")
    public ResponseEntity<Map<String, Object>> getLeadTimeMetrics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        // Si no se proporcionan fechas, usar último mes
        LocalDateTime end = endDate != null ? endDate : LocalDateTime.now();
        LocalDateTime start = startDate != null ? startDate : end.minusMonths(1);

        return ResponseEntity.ok(changeRequestService.calculateLeadTimeMetrics(start, end));
    }

    @GetMapping("/commit/{commitId}")
    public ResponseEntity<List<ChangeRequest>> getByCommitId(@PathVariable String commitId) {
        return ResponseEntity.ok(changeRequestService.findByCommitId(commitId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ChangeRequest>> getByStatus(@PathVariable ChangeRequestStatus status) {
        return ResponseEntity.ok(changeRequestService.getChangeRequestsByStatus(status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChangeRequest(@PathVariable Long id) {
        changeRequestService.deleteChangeRequest(id);
        return ResponseEntity.ok().build();
    }
}