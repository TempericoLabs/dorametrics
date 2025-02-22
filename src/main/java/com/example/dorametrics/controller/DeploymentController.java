package com.example.dorametrics.controller;

import com.example.dorametrics.model.deployment.Deployment;
import com.example.dorametrics.service.DeploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deployments")
@RequiredArgsConstructor
public class DeploymentController {
    private final DeploymentService service;

    @GetMapping
    public ResponseEntity<List<Deployment>> getAllDeployments() {
        return ResponseEntity.ok(service.getAllDeployments());
    }

    @PostMapping
    public ResponseEntity<Deployment> registerDeployment(@RequestBody Deployment deployment) {
        return ResponseEntity.ok(service.registerDeployment(deployment));
    }

    @GetMapping("/metrics/frequency")
    public ResponseEntity<Map<String, Object>> getDeploymentFrequency(
            @RequestParam(required = false) String environment,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(service.calculateDeploymentFrequency(environment, startDate, endDate));
    }
}