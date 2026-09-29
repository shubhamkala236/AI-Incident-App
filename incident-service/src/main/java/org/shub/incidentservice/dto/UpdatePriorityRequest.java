package org.shub.incidentservice.dto;

import jakarta.validation.constraints.NotNull;
import org.shub.incidentservice.entity.IncidentPriority;

public record UpdatePriorityRequest(
        @NotNull(message = "Priority is required")
        IncidentPriority priority
) {
}
