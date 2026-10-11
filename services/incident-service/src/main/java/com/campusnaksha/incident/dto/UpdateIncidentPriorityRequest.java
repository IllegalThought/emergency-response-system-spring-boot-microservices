package com.campusnaksha.incident.dto;

import com.campusnaksha.incident.entity.IncidentPriority;

import jakarta.validation.constraints.NotNull;

public record UpdateIncidentPriorityRequest(

        @NotNull(message = "Priority is required")
        IncidentPriority priority

) {
}