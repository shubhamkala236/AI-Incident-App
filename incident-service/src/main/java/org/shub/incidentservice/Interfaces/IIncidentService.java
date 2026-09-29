package org.shub.incidentservice.Interfaces;

import org.shub.incidentservice.dto.CreateIncidentRequest;
import org.shub.incidentservice.dto.IncidentResponse;
import org.shub.incidentservice.dto.UpdateIncidentRequest;
import org.shub.incidentservice.entity.IncidentPriority;
import org.shub.incidentservice.entity.IncidentStatus;

import java.util.List;

public interface IIncidentService {
    IncidentResponse create(CreateIncidentRequest request, Long reportedBy);

    /** All filters are optional; q matches title or description, case-insensitively. */
    List<IncidentResponse> search(String q, IncidentStatus status,
                                  IncidentPriority priority, Long assignedTo);

    IncidentResponse get(Long id);

    IncidentResponse update(Long id, UpdateIncidentRequest request);

    void delete(Long id);

    IncidentResponse changeStatus(Long id, IncidentStatus status);

    IncidentResponse changePriority(Long id, IncidentPriority priority);

    IncidentResponse assign(Long id, Long assignedTo);
}
