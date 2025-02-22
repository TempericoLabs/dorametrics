package com.example.dorametrics.model.changerequest;

public enum ChangeRequestStatus {
    PENDING,        // Cuando se crea el cambio/commit
    IN_REVIEW,      // En proceso de revisión de código
    APPROVED,       // Aprobado pero pendiente de implementar
    REJECTED,       // Cambio rechazado
    IMPLEMENTING,   // En proceso de despliegue
    IMPLEMENTED,    // Desplegado exitosamente
    FAILED         // Falló durante el despliegue
}