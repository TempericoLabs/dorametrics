package com.example.dorametrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * DORA Metrics Application
 * This application tracks and measures DORA (DevOps Research and Assessment) metrics:
 * - Deployment Frequency
 * - Lead Time for Changes
 * - Time to Restore Service
 * - Change Failure Rate
 *
 * VAMOS 7
 */
@SpringBootApplication
public class DoraMetricsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoraMetricsApplication.class, args);
    }
}