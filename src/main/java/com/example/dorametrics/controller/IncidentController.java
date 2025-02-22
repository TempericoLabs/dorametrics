package com.example.dorametrics.controller;

import com.example.dorametrics.model.incident.Incident;
import com.example.dorametrics.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/incident")
@RequiredArgsConstructor
public class IncidentController {
    private final IncidentService service;

    // Implementar endpoints
}
