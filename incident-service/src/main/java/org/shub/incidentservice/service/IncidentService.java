package org.shub.incidentservice.service;

import jakarta.persistence.criteria.Predicate;
import org.shub.incidentservice.Interfaces.IIncidentService;
import org.shub.incidentservice.dto.CreateIncidentRequest;
import org.shub.incidentservice.dto.IncidentResponse;
import org.shub.incidentservice.dto.UpdateIncidentRequest;
import org.shub.incidentservice.entity.Incident;
import org.shub.incidentservice.entity.IncidentPriority;
import org.shub.incidentservice.entity.IncidentStatus;
import org.shub.incidentservice.exception.IncidentNotFoundException;
import org.shub.incidentservice.repository.IncidentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class IncidentService implements IIncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Override
    @Transactional
    public IncidentResponse create(CreateIncidentRequest request, Long reportedBy) {
        Incident incident = new Incident();
        incident.setTitle(request.title().trim());
        incident.setDescription(request.description());
        if (request.priority() != null) incident.setPriority(request.priority());
        incident.setReportedBy(reportedBy);
        return IncidentResponse.from(incidentRepository.save(incident));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentResponse> search(String q, IncidentStatus status,
                                         IncidentPriority priority, Long assignedTo) {
        Specification<Incident> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (q != null && !q.isBlank()) {
                String pattern = "%" + q.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.<String>get("description"), "")), pattern)));
            }
            if (status != null) predicates.add(cb.equal(root.get("status"), status));
            if (priority != null) predicates.add(cb.equal(root.get("priority"), priority));
            if (assignedTo != null) predicates.add(cb.equal(root.get("assignedTo"), assignedTo));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return incidentRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream().map(IncidentResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public IncidentResponse get(Long id) {
        return IncidentResponse.from(find(id));
    }

    @Override
    @Transactional
    public IncidentResponse update(Long id, UpdateIncidentRequest request) {
        Incident incident = find(id);

        if (request.title() != null) incident.setTitle(request.title().trim());
        if (request.description() != null) incident.setDescription(request.description());
        incident.setUpdatedAt(LocalDateTime.now());

        return IncidentResponse.from(incidentRepository.save(incident));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        incidentRepository.delete(find(id));
    }

    @Override
    @Transactional
    public IncidentResponse changeStatus(Long id, IncidentStatus status) {
        Incident incident = find(id);
        LocalDateTime now = LocalDateTime.now();
        incident.setStatus(status);
        boolean done = status == IncidentStatus.RESOLVED || status == IncidentStatus.CLOSED;
        if (done && incident.getResolvedAt() == null) {
            incident.setResolvedAt(now);
        } else if (!done) {
            incident.setResolvedAt(null);
        }
        incident.setClosedAt(status == IncidentStatus.CLOSED ? now : null);
        incident.setUpdatedAt(now);
        return IncidentResponse.from(incidentRepository.save(incident));
    }

    @Override
    @Transactional
    public IncidentResponse changePriority(Long id, IncidentPriority priority) {
        Incident incident = find(id);
        incident.setPriority(priority);
        incident.setUpdatedAt(LocalDateTime.now());
        return IncidentResponse.from(incidentRepository.save(incident));
    }

    @Override
    @Transactional
    public IncidentResponse assign(Long id, Long assignedTo) {
        Incident incident = find(id);
        incident.setAssignedTo(assignedTo);
        incident.setUpdatedAt(LocalDateTime.now());
        return IncidentResponse.from(incidentRepository.save(incident));
    }

    private Incident find(Long id) {
        return incidentRepository.findById(id).orElseThrow(() -> new IncidentNotFoundException(id));
    }
}
