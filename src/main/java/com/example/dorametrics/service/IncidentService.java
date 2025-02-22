package com.example.dorametrics.service;

import com.example.dorametrics.model.incident.Incident;
import com.example.dorametrics.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository repository;

    // Implementar métodos del servicio
}
