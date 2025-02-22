package com.example.dorametrics.service;

import com.example.dorametrics.model.deployment.Deployment;
import com.example.dorametrics.model.deployment.DeploymentStatus;
import com.example.dorametrics.repository.DeploymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeploymentService {
    private final DeploymentRepository repository;

    public List<Deployment> getAllDeployments() {
        return repository.findAll();
    }

    public Deployment registerDeployment(Deployment deployment) {
        if (deployment.getStartTime() == null) {
            deployment.setStartTime(LocalDateTime.now());
        }
        if (deployment.getEndTime() == null) {
            deployment.setEndTime(LocalDateTime.now());
        }
        return repository.save(deployment);
    }

    public Map<String, Object> calculateDeploymentFrequency(
            String environment, LocalDate startDate, LocalDate endDate) {

        // Si no se proporcionan fechas, usar último mes
        LocalDateTime start = startDate != null ?
                startDate.atStartOfDay() :
                LocalDateTime.now().minusMonths(1);
        LocalDateTime end = endDate != null ?
                endDate.atTime(23, 59, 59) :
                LocalDateTime.now();

        List<Deployment> deployments;
        if (environment != null) {
            deployments = repository.findByEnvironmentAndStartTimeBetween(
                    environment, start, end);
        } else {
            deployments = repository.findByStartTimeBetween(start, end);
        }

        long totalDays = ChronoUnit.DAYS.between(start, end) + 1;
        long successfulDeployments = deployments.stream()
                .filter(d -> d.getStatus() == DeploymentStatus.SUCCESS)
                .count();

        return Map.of(
                "totalDeployments", deployments.size(),
                "successfulDeployments", successfulDeployments,
                "deploymentsPerDay", (double) deployments.size() / totalDays,
                "periodStart", start,
                "periodEnd", end,
                "environment", environment != null ? environment : "all"
        );
    }

    // Nuevos métodos para PowerBI
    public Map<String, Object> getDeploymentFrequencyForPowerBI(
            String environment,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        // Usar la lógica existente pero adaptada para PowerBI
        Map<String, Object> basicMetrics = calculateDeploymentFrequency(
                environment,
                startDate != null ? startDate.toLocalDate() : null,
                endDate != null ? endDate.toLocalDate() : null
        );

        // Añadir métricas adicionales específicas para PowerBI
        Map<String, Object> result = new HashMap<>(basicMetrics);

        List<Deployment> deployments = environment != null ?
                repository.findByEnvironmentAndStartTimeBetween(environment, startDate, endDate) :
                repository.findByStartTimeBetween(startDate, endDate);

        // Agrupamos por día para visualización en PowerBI
        Map<String, Long> deploymentsByDay = deployments.stream()
                .collect(Collectors.groupingBy(
                        deployment -> deployment.getStartTime().toLocalDate().toString(),
                        Collectors.counting()
                ));

        result.put("deploymentsByDay", deploymentsByDay);

        // Añadir distribución por estado
        Map<DeploymentStatus, Long> deploymentsByStatus = deployments.stream()
                .collect(Collectors.groupingBy(
                        Deployment::getStatus,
                        Collectors.counting()
                ));

        result.put("deploymentsByStatus", deploymentsByStatus);

        return result;
    }

    public Map<String, Object> getDeploymentSummaryForPowerBI() {
        List<Deployment> allDeployments = repository.findAll();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalDeployments", allDeployments.size());

        // Distribución por ambiente
        Map<String, Long> deploymentsByEnvironment = allDeployments.stream()
                .collect(Collectors.groupingBy(
                        Deployment::getEnvironment,
                        Collectors.counting()
                ));
        summary.put("deploymentsByEnvironment", deploymentsByEnvironment);

        // Tasa de éxito global
        long successfulDeployments = allDeployments.stream()
                .filter(d -> d.getStatus() == DeploymentStatus.SUCCESS)
                .count();

        summary.put("successRate", (double) successfulDeployments / allDeployments.size());

        return summary;
    }

    public Map<String, Object> getDeploymentTrendsForPowerBI(
            String environment,
            Integer lastNDays) {

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(lastNDays != null ? lastNDays : 30);

        return getDeploymentFrequencyForPowerBI(environment, startDate, endDate);
    }
}