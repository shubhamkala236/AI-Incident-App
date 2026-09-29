package org.shub.incidentservice.exception;

public class IncidentNotFoundException extends RuntimeException {
    public IncidentNotFoundException(Long id) {
        super("Incident " + id + " not found");
    }
}
