package com.example.dorametrics.controller;

import com.example.dorametrics.model.deployment.Deployment;
import com.example.dorametrics.service.DeploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/powerbi")
@RequiredArgsConstructor
public class PowerBIController {

    private final DeploymentService deploymentService;



    @GetMapping("/deployments/frequency")
    public ResponseEntity<Map<String, Object>> getDeploymentFrequencyForPowerBI(
            @RequestParam(required = false) String environment,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        Map<String, Object> powerBIData = deploymentService.getDeploymentFrequencyForPowerBI(
                environment,
                startDate,
                endDate
        );

        return ResponseEntity.ok(powerBIData);
    }

    @GetMapping("/deployments/summary")
    public ResponseEntity<List<Map<String, Object>>> getDeploymentSummaryForPowerBI() {
        Map<String, Object> summary = deploymentService.getDeploymentSummaryForPowerBI();

        List<Map<String, Object>> result = new ArrayList<>();

        // Formato simple: métrica y valor
        Map<String, Object> totalDeployments = new HashMap<>();
        totalDeployments.put("metric", "Total Deployments");
        totalDeployments.put("value", summary.getOrDefault("totalDeployments", 0));
        result.add(totalDeployments);

        Map<String, Object> successRate = new HashMap<>();
        successRate.put("metric", "Success Rate");
        successRate.put("value", summary.getOrDefault("successRate", 0.0));
        result.add(successRate);

        Map<String, Object> production = new HashMap<>();
        production.put("metric", "Production");
        production.put("value", summary.getOrDefault("productionDeployments", 0));
        result.add(production);

        Map<String, Object> staging = new HashMap<>();
        staging.put("metric", "Staging");
        staging.put("value", summary.getOrDefault("stagingDeployments", 0));
        result.add(staging);

        Map<String, Object> development = new HashMap<>();
        development.put("metric", "Development");
        development.put("value", summary.getOrDefault("developmentDeployments", 0));
        result.add(development);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/deployments/trends")
    public ResponseEntity<Map<String, Object>> getDeploymentTrendsForPowerBI(
            @RequestParam(required = false) String environment,
            @RequestParam(required = false) Integer lastNDays) {

        Map<String, Object> trends = deploymentService.getDeploymentTrendsForPowerBI(
                environment,
                lastNDays
        );

        return ResponseEntity.ok(trends);
    }
}