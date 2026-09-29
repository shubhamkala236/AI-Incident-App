package org.shub.incidentservice.controller;

import jakarta.validation.Valid;
import org.shub.incidentservice.Interfaces.IIncidentService;
import org.shub.incidentservice.dto.ApiResponse;
import org.shub.incidentservice.dto.CreateIncidentRequest;
import org.shub.incidentservice.dto.IncidentResponse;
import org.shub.incidentservice.dto.UpdateAssigneeRequest;
import org.shub.incidentservice.dto.UpdateIncidentRequest;
import org.shub.incidentservice.dto.UpdatePriorityRequest;
import org.shub.incidentservice.dto.UpdateStatusRequest;
import org.shub.incidentservice.entity.IncidentPriority;
import org.shub.incidentservice.entity.IncidentStatus;
import org.shub.incidentservice.security.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IIncidentService incidentService;

    public IncidentController(IIncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<IncidentResponse>> create(@Valid @RequestBody CreateIncidentRequest request,
                                                                @AuthenticationPrincipal AuthenticatedUser user) {
        IncidentResponse response = incidentService.create(request, user.userId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Incident created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<IncidentResponse>>> list(@RequestParam(required = false) String q,
                                                                    @RequestParam(required = false) IncidentStatus status,
                                                                    @RequestParam(required = false) IncidentPriority priority,
                                                                    @RequestParam(required = false) Long assignedTo) {
        return ResponseEntity.ok(ApiResponse.success("Incidents retrieved",
                incidentService.search(q, status, priority, assignedTo)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IncidentResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Incident retrieved", incidentService.get(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<IncidentResponse>> update(@PathVariable Long id,
                                                                @Valid @RequestBody UpdateIncidentRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Incident updated successfully", incidentService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        incidentService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Incident deleted successfully", null));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<IncidentResponse>> changeStatus(@PathVariable Long id,
                                                                      @Valid @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Incident status updated",
                incidentService.changeStatus(id, request.status())));
    }

    @PatchMapping("/{id}/priority")
    public ResponseEntity<ApiResponse<IncidentResponse>> changePriority(@PathVariable Long id,
                                                                        @Valid @RequestBody UpdatePriorityRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Incident priority updated",
                incidentService.changePriority(id, request.priority())));
    }

    @PatchMapping("/{id}/assignee")
    public ResponseEntity<ApiResponse<IncidentResponse>> assign(@PathVariable Long id,
                                                                @RequestBody UpdateAssigneeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Incident assignee updated",
                incidentService.assign(id, request.assignedTo())));
    }
}
