package com.campusnaksha.incident.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.campusnaksha.incident.dto.CreateIncidentRequest;
import com.campusnaksha.incident.dto.IncidentResponse;
import com.campusnaksha.incident.dto.IncidentStatusHistoryResponse;
import com.campusnaksha.incident.dto.UpdateIncidentPriorityRequest;
import com.campusnaksha.incident.dto.UpdateIncidentStatusRequest;
import com.campusnaksha.incident.entity.IncidentCategory;
import com.campusnaksha.incident.entity.IncidentStatus;
import com.campusnaksha.incident.service.IncidentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService service;

    public IncidentController(
            IncidentService service
    ) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public IncidentResponse create(
            @Valid @RequestBody CreateIncidentRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {

        UUID reportedByUserId =
                UUID.fromString(jwt.getSubject());

        return service.create(
                request,
                reportedByUserId
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'GUARD', 'ADMIN')")
    public List<IncidentResponse> getAll(
            @RequestParam(required = false)
            IncidentStatus status,

            @RequestParam(required = false)
            IncidentCategory category
    ) {

        return service.findAll(
                status,
                category
        );
    }

    @GetMapping("/map")
    @PreAuthorize("hasAnyRole('STUDENT', 'GUARD', 'ADMIN')")
    public List<IncidentResponse> getMap() {

        return service.findAll(
                null,
                null
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'GUARD', 'ADMIN')")
    public IncidentResponse getById(
            @PathVariable UUID id
    ) {

        return service.findById(id);
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('STUDENT', 'GUARD', 'ADMIN')")
    public List<IncidentStatusHistoryResponse> getHistory(
            @PathVariable UUID id
    ) {

        return service.findHistory(id);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public IncidentResponse updateStatus(
            @PathVariable UUID id,

            @Valid
            @RequestBody
            UpdateIncidentStatusRequest request,

            @AuthenticationPrincipal Jwt jwt
    ) {

        UUID changedByUserId =
                UUID.fromString(jwt.getSubject());

        return service.updateStatus(
                id,
                request,
                changedByUserId
        );
    }

    @PatchMapping("/{id}/priority")
    @PreAuthorize("hasRole('ADMIN')")
    public IncidentResponse updatePriority(
            @PathVariable UUID id,

            @Valid
            @RequestBody
            UpdateIncidentPriorityRequest request
    ) {

        return service.updatePriority(
                id,
                request
        );
    }
}