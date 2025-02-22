package com.example.dorametrics.repository;

import com.example.dorametrics.model.deployment.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeploymentRepository extends JpaRepository<Deployment, Long> {
    List<Deployment> findByEnvironment(String environment);
    List<Deployment> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Deployment> findByEnvironmentAndStartTimeBetween(
            String environment,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}