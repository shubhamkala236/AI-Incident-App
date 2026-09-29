package org.shub.incidentservice.dto;

import org.shub.incidentservice.entity.Incident;
import org.shub.incidentservice.entity.IncidentPriority;
import org.shub.incidentservice.entity.IncidentStatus;

import java.time.LocalDateTime;

public record IncidentResponse(
        Long id,
        String title,
        String description,
        IncidentPriority priority,
        IncidentStatus status,
        Long reportedBy,
        Long assignedTo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime resolvedAt
) {
    public static IncidentResponse from(Incident i) {
        return new IncidentResponse(i.getId(), i.getTitle(), i.getDescription(), i.getPriority(),
                i.getStatus(), i.getReportedBy(), i.getAssignedTo(), i.getCreatedAt(), i.getUpdatedAt(), i.getResolvedAt());
    }
}
