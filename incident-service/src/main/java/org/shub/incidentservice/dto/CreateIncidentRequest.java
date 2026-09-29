package org.shub.incidentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.shub.incidentservice.entity.IncidentPriority;

public record CreateIncidentRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must be at most 200 characters")
        String title,

        @Size(max = 4000, message = "Description must be at most 4000 characters")
        String description,

        /** Defaults to MEDIUM when omitted. */
        IncidentPriority priority
) {
}
