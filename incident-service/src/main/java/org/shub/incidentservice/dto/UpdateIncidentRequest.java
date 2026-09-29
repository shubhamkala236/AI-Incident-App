package org.shub.incidentservice.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Partial update: null fields are left unchanged. Status, priority and assignee have their own endpoints. */
public record UpdateIncidentRequest(

        @Pattern(regexp = ".*\\S.*", message = "Title must not be blank")
        @Size(max = 200, message = "Title must be at most 200 characters")
        String title,

        @Size(max = 4000, message = "Description must be at most 4000 characters")
        String description
) {
}
