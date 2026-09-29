package org.shub.incidentservice.dto;

/** A null assignedTo unassigns the incident. */
public record UpdateAssigneeRequest(
        Long assignedTo
) {
}
