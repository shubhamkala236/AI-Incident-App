package org.shub.incidentservice.dto;

import jakarta.validation.constraints.NotNull;
import org.shub.incidentservice.entity.IncidentStatus;

public record UpdateStatusRequest(
        @NotNull(message = "Status is required")
        IncidentStatus status
) {
}
